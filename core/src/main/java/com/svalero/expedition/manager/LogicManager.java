package com.svalero.expedition.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapLayer;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.svalero.expedition.domain.Player;
import com.svalero.expedition.domain.Relic;
import com.svalero.expedition.domain.Supply;
import com.svalero.expedition.domain.Guardian;
import com.svalero.expedition.domain.Bird;
import com.svalero.expedition.domain.ImmunityItem;
import com.svalero.expedition.domain.ScoreItem;
import com.svalero.expedition.domain.PoisonItem;

public class LogicManager {

    private static final float PLAYER_START_X = 20;
    private static final float PLAYER_START_Y = 310;

    private static final float SUPPLY_START_X = 36;
    private static final float SUPPLY_START_Y = 100;
    private static final int SUPPLY_HEALTH_AMOUNT = 20;
    private static final float SUPPLY_CALL_COOLDOWN = 15f;
    private static final float SUPPLY_FOLLOW_EXTRA_DISTANCE = 32f;
    private static final float SUPPLY_CALL_SPEED = 90f;
    private static final float SUPPLY_FOLLOW_SPEED = 70f;
    private static final float LEVEL_2_SUPPLY_CALL_SPEED = 120f;
    private static final float LEVEL_2_SUPPLY_FOLLOW_SPEED = 90f;

    private static final float POISON_DURATION = 5f;
    private static final float POISON_SPEED_FACTOR = 0.2f;

    private static final float SPEED_BOOST_DURATION = 4f;
    private static final float SPEED_BOOST_FACTOR = 1.8f;

    private static final float LEVEL_2_BIRD_SPEED = 250f;

    private final Player player;
    private Relic relic;
    private final Supply supply;
    private Guardian guardian;
    private final Bird bird;
    private ScoreItem scoreItem;
    private ImmunityItem immunityItem;
    private PoisonItem poisonItem;

    private final ConfigurationManager configurationManager;


    // timers
    private float guardianDamageTimer; // tiempo de espera entre daños consecutivos.
    private float birdDamageTimer; //tiempo de espera entre daños consecutivos
    private float scoreMessageTimer;
    private float supplyCooldownTimer;
    private float supplyUnavailableMessageTimer; // Mensaje ayuda de Frodo no disponible
    private float supplyHealMessageTimer;
    private int lastSupplyHealAmount;
    private float birdHitMessageTimer;
    private float immunityMessageTimer;
    private float poisonTimer;
    private float poisonMessageTimer;
    private float guardianDeathMessageTimer;
    private boolean birdTriggered;
    private float birdAttackCooldownTimer;

    // posiciones iniciales cargadas desdeTiled
    private float playerStartX;
    private float playerStartY;
    private float supplyStartX;
    private float supplyStartY;
    private float birdStartX;
    private float birdStartY;
    private float birdZoneX;
    private float birdZoneY;
    private float birdZoneWidth;
    private float birdZoneHeight;
    private boolean birdZoneConfigured;

    private float worldWidth;
    private float worldHeight;
    private TiledMapTileLayer collisionLayer;
    private float tileWidth;
    private float tileHeight;

    private float friendZoneX;
    private float friendZoneY;
    private float friendZoneWidth;
    private float friendZoneHeight;
    private boolean friendZoneConfigured;
    private boolean friendMessageTriggered;
    private float friendMessageTimer;

    private final int currentLevel;

    private float speedBoostTimer;

    // Ubicación
    public LogicManager(int currentLevel) {
        configurationManager = new ConfigurationManager();
        this.currentLevel = currentLevel;

        player = new Player(PLAYER_START_X, PLAYER_START_Y, 150, 100, 3, 0);
        relic = new Relic(600, 220);
        scoreItem = new ScoreItem(320, 220, 24, 24, 25);
        supply = new Supply(SUPPLY_START_X, SUPPLY_START_Y, 0);
        guardian = new Guardian(500, 140, 100, 150, 280);
        immunityItem = new ImmunityItem(200, 300, 24, 24);
        poisonItem = new PoisonItem(400, 300, 24, 24);

        float birdSpeed = (currentLevel == 2) ? LEVEL_2_BIRD_SPEED : 450f;
        bird = new Bird(-60, Gdx.graphics.getHeight() - 36, birdSpeed, 320);

        guardianDamageTimer = 0;
        birdDamageTimer = 0;
        scoreMessageTimer = 0;
        supplyCooldownTimer = 0;
        supplyUnavailableMessageTimer = 0;
        supplyHealMessageTimer = 0;
        lastSupplyHealAmount = 0;
        birdHitMessageTimer = 0;
        immunityMessageTimer = 0;
        poisonTimer = 0;
        poisonMessageTimer = 0;
        speedBoostTimer= 0;
        guardianDeathMessageTimer = 0;
        birdTriggered = false;
        birdAttackCooldownTimer = 0;

        worldWidth = Gdx.graphics.getWidth();
        worldHeight = Gdx.graphics.getHeight();

        // Valores por defecto si el mapa no los sobrescribe
        playerStartX = PLAYER_START_X;
        playerStartY = PLAYER_START_Y;

        supplyStartX = SUPPLY_START_X;
        supplyStartY = SUPPLY_START_Y;

        birdStartX = -60;
        birdStartY = Gdx.graphics.getHeight() - 36;

        birdZoneX = 0;
        birdZoneY = 0;
        birdZoneWidth = 0;
        birdZoneHeight = 0;
        birdZoneConfigured = false;

        friendZoneX = 0;
        friendZoneY = 0;
        friendZoneWidth = 0;
        friendZoneHeight = 0;
        friendZoneConfigured = false;
        friendMessageTriggered = false;
        friendMessageTimer = 0;

        if (currentLevel == 2) {
            bird.setIdlePosition(birdStartX, birdStartY);
            bird.setAlwaysVisible(true);
        }
    }

    // -- SETUP -- Métodos de configuración inicial

    public void setWorldSize(float worldWidth, float worldHeight) {
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
    }

    public void setCollisionLayer(TiledMapTileLayer collisionLayer, float tileWidth, float tileHeight) {
        this.collisionLayer = collisionLayer;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
    }

    public void loadMapObjects(MapLayer objectsLayer) {

        if (objectsLayer == null) {
            System.err.println("ERROR: La capa 'Objects' es null. Asegúrate de que la capa en Tiled se llama exactamente 'Objects' (sensitive Case).");
            return;
        }

        MapObjects objects = objectsLayer.getObjects();

        for (MapObject object : objects) {

            // Tratando de eliminar problema de incompatibilidad de nombre entre versiones
            String type = (String) object.getProperties().get("type");
            if (type == null) {
                type = (String) object.getProperties().get("class");
            }
            if (type == null) {
                type = object.getName();
            }

            if (type == null) {
                continue;
            }

            Object propX = object.getProperties().get("x");
            Object propY = object.getProperties().get("y");

            if (propX == null || propY == null) {
                continue;
            }

            float scale = 2f;
            float x = ((Number) propX).floatValue() * scale;
            float y = ((Number) propY).floatValue() * scale;

            switch (type) {
                case "player":
                    player.setX(x);
                    player.setY(y);
                    playerStartX = x;
                    playerStartY = y;
                    break;

                case "supply":
                    supply.setX(x);
                    supply.setY(y);
                    supplyStartX = x;
                    supplyStartY = y;
                    break;

                case "relic":
                    relic = new Relic(x, y);
                    break;

                case "scoreItem":
                    scoreItem = new ScoreItem(x, y, 24, 24, 25);
                    break;

                case "immunityItem":
                    immunityItem = new ImmunityItem(x, y, 24, 24);
                    break;

                case "poisonItem":
                    poisonItem = new PoisonItem(x, y, 24, 24);
                    break;

                case "guardian":
                    Object propSpeed = object.getProperties().get("speed");

                    float speed = (propSpeed != null) ? ((Number) propSpeed).floatValue() : 100f;

                    if (currentLevel == 2) {
                        Object propPointBX = object.getProperties().get("pointBX");
                        Object propPointBY = object.getProperties().get("pointBY");
                        Object propPointCX = object.getProperties().get("pointCX");
                        Object propPointCY = object.getProperties().get("pointCY");
                        Object propWaitTime = object.getProperties().get("waitTime");

                        float pointBX = (propPointBX != null) ? ((Number) propPointBX).floatValue() * scale : x;
                        float pointBY = (propPointBY != null) ? ((Number) propPointBY).floatValue() * scale : y;
                        float pointCX = (propPointCX != null) ? ((Number) propPointCX).floatValue() * scale : x;
                        float pointCY = (propPointCY != null) ? ((Number) propPointCY).floatValue() * scale : y;
                        float waitTime = (propWaitTime != null) ? ((Number) propWaitTime).floatValue() : 1f;

                        guardian = new Guardian(x, y, speed, pointBX, pointBY, pointCX, pointCY, waitTime);
                    } else {
                        Object propMinY = object.getProperties().get("minY");
                        Object propMaxY = object.getProperties().get("maxY");

                        float minY = (propMinY != null) ? ((Number) propMinY).floatValue() * scale : 240 * scale;
                        float maxY = (propMaxY != null) ? ((Number) propMaxY).floatValue() * scale : 280 * scale;

                        guardian = new Guardian(x, y, speed, minY, maxY);
                    }
                    break;

                case "bird":
                    birdStartX = x;
                    birdStartY = y;

                    if (currentLevel == 2) {
                        bird.setIdlePosition(x, y);
                        bird.setAlwaysVisible(true);
                    } else {
                        bird.resetPosition(x, y);
                        bird.setAlwaysVisible(false);
                    }
                    break;

                case "birdZone":
                    if (object instanceof RectangleMapObject rectangleMapObject) {
                        Rectangle rectangle = rectangleMapObject.getRectangle();

                        birdZoneX = rectangle.x * scale;
                        birdZoneY = rectangle.y * scale;
                        birdZoneWidth = rectangle.width * scale;
                        birdZoneHeight = rectangle.height * scale;

                        birdZoneConfigured = true;
                    }
                    break;

                case "friendZone":
                    if (object instanceof RectangleMapObject rectangleMapObject) {
                        Rectangle rectangle = rectangleMapObject.getRectangle();

                        friendZoneX = rectangle.x * scale;
                        friendZoneY = rectangle.y * scale;
                        friendZoneWidth = rectangle.width * scale;
                        friendZoneHeight = rectangle.height * scale;

                        friendZoneConfigured = true;
                    }
                    break;

                default:
                    System.out.println("Objeto de tipo no reconocido: " + type);
                    break;
            }
        }
    }

    private void playSound(String path) {
        // Solo reproduce efectos si el sonido está activado en configuración
        if (!configurationManager.isSoundEnabled()) {
            return;
        }

        ResourceManager.getSound(path).play();
    }

    private void updateBirdZoneTrigger() {
        if (currentLevel != 2) {
            return;
        }

        if (!birdZoneConfigured || bird == null) {
            return;
        }

        float playerCenterX = getPlayerHitboxCenterX();
        float playerCenterY = getPlayerHitboxCenterY();

        boolean insideZoneX = playerCenterX >= birdZoneX && playerCenterX <= birdZoneX + birdZoneWidth;
        boolean insideZoneY = playerCenterY >= birdZoneY && playerCenterY <= birdZoneY + birdZoneHeight;

        if (insideZoneX && insideZoneY && bird.isIdle() && birdAttackCooldownTimer <= 0) {
            activateBird();
            birdTriggered = true;
        }

        if (!insideZoneX || !insideZoneY) {
            birdTriggered = false;
        }
    }

    private void updateBirdTriggerByItem(float itemX, float itemY, float itemWidth, float itemHeight, float triggerDistance) {
        if (currentLevel != 1) {
            return;
        }

        if (bird == null) {
            return;
        }

        boolean nearX = getPlayerHitboxLeft() < itemX + itemWidth + triggerDistance
            && getPlayerHitboxRight() > itemX - triggerDistance;

        boolean nearY = getPlayerHitboxBottom() < itemY + itemHeight + triggerDistance
            && getPlayerHitboxTop() > itemY - triggerDistance;

        if (nearX && nearY && !birdTriggered && !bird.isActive()) {
            activateBird();
            birdTriggered = true;
        } else if ((!nearX || !nearY) && !bird.isActive()) {
            birdTriggered = false;
        }
    }

    private void updateFriendZoneTrigger() {
        if (currentLevel != 2) {
            return;
        }

        if (!friendZoneConfigured || friendMessageTriggered) {
            return;
        }

        boolean overlapX = getPlayerHitboxRight() >= friendZoneX
            && getPlayerHitboxLeft() <= friendZoneX + friendZoneWidth;

        boolean overlapY = getPlayerHitboxTop() >= friendZoneY
            && getPlayerHitboxBottom() <= friendZoneY + friendZoneHeight;

        if (overlapX && overlapY) {
            friendMessageTriggered = true;
            friendMessageTimer = 3f;
        }
    }


    // --- CORE LOOP --- Lógica que se ejecuta continuamente

    public void update(float delta) {
        handleInput();

        updateGuardianDamageTimer(delta);
        updateBirdDamageTimer(delta);
        updateScoreMessageTimer(delta);
        updateSupplyCooldownTimer(delta);
        updateSupplyUnavailableMessageTimer(delta);
        updateSupplyHealMessageTimer(delta);
        updateBirdHitMessageTimer(delta);
        updateImmunityMessageTimer(delta);
        updatePoisonTimer(delta);
        updatePoisonMessageTimer(delta);
        updateBirdDamageTimer(delta);
        updateBirdAttackCooldownTimer(delta);
        updateSpeedBoostTimer(delta);
        updateGuardianDeathMessageTimer(delta);
        updateFriendMessageTimer(delta);

        if (guardianDamageTimer <= 0 && birdDamageTimer <= 0) {
            float speedFactor = 1f;

            if (poisonTimer > 0) {
                speedFactor = POISON_SPEED_FACTOR;
            } else if (speedBoostTimer > 0) {
                speedFactor = SPEED_BOOST_FACTOR;
            }

            player.setSpeedMultiplier(speedFactor);
            movePlayer(delta);
            guardian.update(delta);
        }

        supply.update(delta);
        moveSupply(delta);
        updateBirdZoneTrigger();
        updateFriendZoneTrigger();

        if (bird != null && bird.isActive()) {
            // bird.setTarget(getPlayerHitboxCenterX(), getPlayerHitboxCenterY());
            bird.update(delta);
            checkBirdCollision();
        }

        resetBirdIfNeeded();

        keepPlayerInsideWorld();
        checkRelicCollision();
        checkScoreItemCollision();
        checkSupplyCollision();
        checkGuardianCollision();
        checkImmunityItemCollision();
        checkPoisonItemCollision();
        checkPlayerEnergy();
    }

    // --- HELPERS --- Métodos de apoyo al bucle principal
        // Ayudan directamente al movimiento y la física básica

    private void handleInput() {

        // Si la niña choca con el oso, no puede moverse ni llamar a Frodo
        if (guardianDamageTimer > 0 || birdDamageTimer > 0) {
            player.setDirectionX(0);
            player.setDirectionY(0);
            return;
        }


        // Se reinicia la dirección en cada frame
        player.setDirectionX(0);
        player.setDirectionY(0);

        // Movimiento horizontal
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.setDirectionX(-1);
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.setDirectionX(1);
        }

        // Movimiento vertical
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            player.setDirectionY(1);
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            player.setDirectionY(-1);
        }

        // Llamada a Frodo solo si necesita curar y no está en espera
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            boolean playerNeedsEnergy = player.getEnergy() < player.getMaxEnergy();
            boolean supplyAvailable = supplyCooldownTimer <= 0;
            boolean supplyNotAlreadyCalled = !supply.isCalled();

            if (playerNeedsEnergy && supplyAvailable && supplyNotAlreadyCalled) {
                supply.setCalled(true);
                // sonido de llamada a frodo
                playSound("sounds/call_supply.wav");
            } else {
                supplyUnavailableMessageTimer = 1.5f;
            }
        }
    }

    private void movePlayer(float delta) {
        float originalX = player.getX();
        float originalY = player.getY();

        float directionX = player.getDirectionX();
        float directionY = player.getDirectionY();

        // Movimiento en X
        player.setDirectionX(directionX);
        player.setDirectionY(0);
        player.update(delta);
        keepPlayerInsideWorld();

        if (isPlayerCollidingWithTerrain()) {
            player.setX(originalX);
        }

        // Movimiento en Y
        player.setDirectionX(0);
        player.setDirectionY(directionY);
        player.update(delta);
        keepPlayerInsideWorld();

        if (isPlayerCollidingWithTerrain()) {
            player.setY(originalY);
        }

        // Restaurar direcciones
        player.setDirectionX(directionX);
        player.setDirectionY(directionY);
    }

    private void moveSupply(float delta) {
        float targetX = player.getX();
        float targetY = player.getY();

        float distanceX = targetX - supply.getX();
        float distanceY = targetY - supply.getY();
        float distance = (float) Math.sqrt(distanceX * distanceX + distanceY * distanceY);

        float stopDistance = supply.isCalled() ? 5f : supply.getFollowDistance() + SUPPLY_FOLLOW_EXTRA_DISTANCE;

        if (distance < stopDistance) {
            return;
        }

        float directionX = distanceX / distance;
        float directionY = distanceY / distance;

        float callSpeed = (currentLevel == 2) ? LEVEL_2_SUPPLY_CALL_SPEED : SUPPLY_CALL_SPEED;
        float followSpeed = (currentLevel == 2) ? LEVEL_2_SUPPLY_FOLLOW_SPEED : SUPPLY_FOLLOW_SPEED;

        float speed = supply.isCalled() ? callSpeed : followSpeed;

        supply.setX(supply.getX() + directionX * speed * delta);
        supply.setY(supply.getY() + directionY * speed * delta);

        if (supply.getX() < 0) {
            supply.setX(0);
        }
        if (supply.getY() < 0) {
            supply.setY(0);
        }
        if (supply.getX() + supply.getWidth() > worldWidth) {
            supply.setX(worldWidth - supply.getWidth());
        }
        if (supply.getY() + supply.getHeight() > worldHeight) {
            supply.setY(worldHeight - supply.getHeight());
        }
    }

    // Establece límite del mundo para que el jugador no pueda sobrepasar el mapa
    private void keepPlayerInsideWorld() {
        float hitboxLeft = getPlayerHitboxLeft();
        float hitboxRight = getPlayerHitboxRight();
        float hitboxBottom = getPlayerHitboxBottom();
        float hitboxTop = getPlayerHitboxTop();

        if (hitboxLeft < 0) {
            player.setX(player.getX() - hitboxLeft);
        }

        if (hitboxBottom < 0) {
            player.setY(player.getY() - hitboxBottom);
        }

        if (hitboxRight > worldWidth) {
            player.setX(player.getX() - (hitboxRight - worldWidth));
        }

        if (hitboxTop > worldHeight) {
            player.setY(player.getY() - (hitboxTop - worldHeight));
        }
    }

    // Hitbox para establecer que el personaje choque con la parte inferior del sprite y con 'toda la caja'.
        // permite un tamaño mayor del personaje para mejor visualización sin que perjudique a la jugabilidad
    private float getPlayerHitboxWidth() {
        return 20f;
    }

    private float getPlayerHitboxHeight() {
        return 16f;
    }

    private float getPlayerHitboxLeft() {
        return player.getX() + (player.getWidth() - getPlayerHitboxWidth()) / 2f;
    }

    private float getPlayerHitboxRight() {
        return getPlayerHitboxLeft() + getPlayerHitboxWidth() - 1;
    }

    private float getPlayerHitboxBottom() {
        return player.getY();
    }

    private float getPlayerHitboxTop() {
        return getPlayerHitboxBottom() + getPlayerHitboxHeight() - 1;
    }

    private float getPlayerHitboxCenterX() {
        return getPlayerHitboxLeft() + getPlayerHitboxWidth() / 2f;
    }

    private float getPlayerHitboxCenterY() {
        return getPlayerHitboxBottom() + getPlayerHitboxHeight() / 2f;
    }

    private boolean isPlayerCollidingWithTerrain() {
        if (collisionLayer == null) {
            return false;
        }

        return isSolidTileAt(getPlayerHitboxLeft(), getPlayerHitboxBottom())
            || isSolidTileAt(getPlayerHitboxRight(), getPlayerHitboxBottom())
            || isSolidTileAt(getPlayerHitboxLeft(), getPlayerHitboxTop())
            || isSolidTileAt(getPlayerHitboxRight(), getPlayerHitboxTop());
    }

    private boolean isSolidTileAt(float worldX, float worldY) {
        int tileX = (int) (worldX / tileWidth);
        int tileY = (int) (worldY / tileHeight);

        if (tileX < 0 || tileX >= collisionLayer.getWidth()) {
            return false;
        }

        if (tileY < 0 || tileY >= collisionLayer.getHeight()) {
            return false;
        }

        TiledMapTileLayer.Cell cell = collisionLayer.getCell(tileX, tileY) ;
        return cell !=null && cell.getTile() !=null;
    }


// --- ENTIDADES E ITEMS ---

    // --- RELIQUIA ---

    private void checkRelicCollision() {
        if (relic.isCollected()) {
            return;
        }

        if (guardianDamageTimer > 0) {
            return;
        }

        // Comprueba si hay solapamiento horizontal entre el jugador y el premio
        boolean collisionX = getPlayerHitboxLeft() < relic.getX() + relic.getWidth()
            && getPlayerHitboxRight() > relic.getX();

        boolean collisionY = getPlayerHitboxBottom() < relic.getY() + relic.getHeight()
            && getPlayerHitboxTop() > relic.getY();

        if (collisionX && collisionY) {
            relic.setCollected(true);
            player.setScore(player.getScore() + 100);

            // Sonido al recoger la reliquia final
            playSound("sounds/relic_collect.wav");
        }
    }

    private void checkScoreItemCollision() {
        if (scoreItem.isCollected()) {
            return;
        }

        if (guardianDamageTimer > 0) {
            return;
        }

        updateBirdTriggerByItem(
            scoreItem.getX(),
            scoreItem.getY(),
            scoreItem.getWidth(),
            scoreItem.getHeight(),
            80f
        );

        boolean collisionX = getPlayerHitboxLeft() < scoreItem.getX() + scoreItem.getWidth()
            && getPlayerHitboxRight() > scoreItem.getX();

        boolean collisionY = getPlayerHitboxBottom() < scoreItem.getY() + scoreItem.getHeight()
            && getPlayerHitboxTop() > scoreItem.getY();

        if (collisionX && collisionY) {
            scoreItem.setCollected(true);
            player.setScore(player.getScore() + scoreItem.getScoreValue());
            scoreMessageTimer = 1.5f;

            // Sonido al recoger item de puntuación
            playSound("sounds/score_collect.wav");
        }
    }

    private void checkImmunityItemCollision() {
        if (immunityItem.isCollected()) {
            return;
        }

        updateBirdTriggerByItem(
            immunityItem.getX(),
            immunityItem.getY(),
            immunityItem.getWidth(),
            immunityItem.getHeight(),
            80f
        );

//        boolean nearX = getPlayerHitboxLeft() < immunityItem.getX() + immunityItem.getWidth() + 80
//            && getPlayerHitboxRight() > immunityItem.getX() - 80;
//
//        boolean nearY = getPlayerHitboxBottom() < immunityItem.getY() + immunityItem.getHeight() + 80
//            && getPlayerHitboxTop() > immunityItem.getY() - 80;
//
//        if (nearX && nearY && !birdTriggered && bird != null && !bird.isActive()) {
//            activateBird();
//            birdTriggered = true;
//        } else if ((!nearX || !nearY) && bird != null && !bird.isActive()) {
//            birdTriggered = false;
//        }

        boolean collisionX = getPlayerHitboxRight() > immunityItem.getX()
            && getPlayerHitboxLeft() < immunityItem.getX() + immunityItem.getWidth();

        boolean collisionY = getPlayerHitboxTop() > immunityItem.getY()
            && getPlayerHitboxBottom() < immunityItem.getY() + immunityItem.getHeight();

        if (collisionX && collisionY) {
            immunityItem.setCollected(true);
            immunityMessageTimer = 1.5f;

            if (currentLevel == 1) {
                player.addImmunity();
            } else {
                speedBoostTimer = SPEED_BOOST_DURATION;
                // Conserva el contador para la puntuación final
                player.addImmunity();
            }

            // Sonido al recoger el item de inmunidad
            playSound("sounds/immunity_collect.wav");
        }
    }

    private void checkPoisonItemCollision() {
        if (poisonItem.isCollected()) {
            return;
        }

        updateBirdTriggerByItem(
            poisonItem.getX(),
            poisonItem.getY(),
            poisonItem.getWidth(),
            poisonItem.getHeight(),
            40f
        );
//        boolean nearX = getPlayerHitboxLeft() < poisonItem.getX() + poisonItem.getWidth() + 40
//            && getPlayerHitboxRight() > poisonItem.getX() - 40;
//
//        boolean nearY = getPlayerHitboxBottom() < poisonItem.getY() + poisonItem.getHeight() + 40
//            && getPlayerHitboxTop() > poisonItem.getY() - 40;
//
//        if (nearX && nearY && !birdTriggered && bird != null && !bird.isActive()) {
//            activateBird();
//            birdTriggered = true;
//        } else if ((!nearX || !nearY) && bird != null && !bird.isActive()) {
//            birdTriggered = false;
//        }

        boolean collisionX = getPlayerHitboxLeft() < poisonItem.getX() + poisonItem.getWidth()
            && getPlayerHitboxRight() > poisonItem.getX();

        boolean collisionY = getPlayerHitboxBottom() < poisonItem.getY() + poisonItem.getHeight()
            && getPlayerHitboxTop() > poisonItem.getY();

        if (collisionX && collisionY) {
            poisonItem.setCollected(true);
            poisonTimer = POISON_DURATION;
            poisonMessageTimer = 1.5f;
            player.addPoison();

            // Sonido al recoger veneno
            playSound("sounds/poison_collect.mp3");
        }
    }

    private void activateBird() {
        if (bird == null || bird.isActive()) {
            return;
        }

        float birdCollisionWidth = 28f;
        float birdCollisionHeight = 28f;

        float playerHitboxLeft = getPlayerHitboxLeft();
        float playerHitboxBottom = getPlayerHitboxBottom();
        float playerHitboxWidth = getPlayerHitboxWidth();
        float playerHitboxHeight = getPlayerHitboxHeight();

        float targetX = playerHitboxLeft + (playerHitboxWidth - birdCollisionWidth) / 2f
            - (bird.getWidth() - birdCollisionWidth) / 2f;

        float targetY = playerHitboxBottom + (playerHitboxHeight - birdCollisionHeight) / 2f
            - (bird.getHeight() - birdCollisionHeight) / 2f;

        bird.activateAttack(targetX, targetY);
    }

    private void checkSupplyCollision() {
        if (!supply.isCalled()) {
            return;
        }

        boolean collisionX = getPlayerHitboxLeft() < supply.getX() + supply.getWidth()
            && getPlayerHitboxRight() > supply.getX();

        boolean collisionY = getPlayerHitboxBottom() < supply.getY() + supply.getHeight()
            && getPlayerHitboxTop() > supply.getY();

        if (collisionX && collisionY) {
            int previousEnergy = player.getEnergy();

            player.setEnergy(player.getEnergy() + SUPPLY_HEALTH_AMOUNT);

            lastSupplyHealAmount = player.getEnergy() - previousEnergy;
            supplyHealMessageTimer = 1.5f;
            // frodo entra en espera después de curar a Emily
            supplyCooldownTimer = SUPPLY_CALL_COOLDOWN;
            supply.setCalled(false);
        }
    }

    private void checkGuardianCollision() {
        boolean playerCollisionX = getPlayerHitboxRight() > guardian.getX()
            && getPlayerHitboxLeft() < guardian.getX() + guardian.getWidth();

        boolean playerCollisionY = getPlayerHitboxTop() > guardian.getY()
            && getPlayerHitboxBottom() < guardian.getY() + guardian.getHeight();

        boolean supplyCollisionX = supply.getX() + supply.getWidth() > guardian.getX()
            && supply.getX() < guardian.getX() + guardian.getWidth();

        boolean supplyCollisionY = supply.getY() + supply.getHeight() > guardian.getY()
            && supply.getY() < guardian.getY() + guardian.getHeight();

        boolean collisionWithGuardian = (playerCollisionX && playerCollisionY)
            || (supplyCollisionX && supplyCollisionY);

        if (collisionWithGuardian && guardianDamageTimer <= 0) {
            // Nivel 1: immunityItem permite pasar a través del guadián
            if (currentLevel == 1) {
                if (player.getImmunityCollected() == 0) {
                    killPlayerByGuardian();
                    guardianDamageTimer = 1.5f;
                }
            } else {
                // Nivel 2: el boost de velocidad no evita el daño del guardián
                killPlayerByGuardian();
                guardianDamageTimer = 1.5f;
            }
        }
    }

    private void checkBirdCollision() {
        float birdCollisionWidth = 28f;
        float birdCollisionHeight = 28f;

        float birdLeft = bird.getX() + (bird.getWidth() - birdCollisionWidth) / 2f;
        float birdRight = birdLeft + birdCollisionWidth;
        float birdBottom = bird.getY() + (bird.getHeight() - birdCollisionHeight) / 2f;
        float birdTop = birdBottom + birdCollisionHeight;

        boolean collisionX = getPlayerHitboxLeft() <= birdRight
            && getPlayerHitboxRight() >= birdLeft;

        boolean collisionY = getPlayerHitboxBottom() <= birdTop
            && getPlayerHitboxTop() >= birdBottom;

        if (collisionX && collisionY && birdDamageTimer <= 0) {
            int damage = player.getEnergy() / 2;
            player.setEnergy(player.getEnergy() - damage);
            // sonido al colisionar con el ave
            playSound("sounds/hurt.wav");

            player.setScore(player.getScore() - 15);

            birdDamageTimer = 1f;
            birdHitMessageTimer = 1.5f;

            if (bird.isMovingRight()) {
                pushPlayerBackSafely(64f, true);
            } else {
                pushPlayerBackSafely(64f, false);
            }

            if (currentLevel == 2) {
                birdAttackCooldownTimer = 2.5f;
            }

            keepPlayerInsideWorld();

            supply.setX(player.getX() + 64);
            supply.setY(player.getY());
            supply.setCalled(false);

            if (supply.getX() + supply.getWidth() > worldWidth) {
                supply.setX(worldWidth - supply.getWidth());
            }
        }
    }

    private void resetBirdIfNeeded() {
        if (bird == null || !bird.isActive()) {
            return;
        }

        boolean outRight = bird.getX() > worldWidth + 60;
        boolean outLeft = bird.getX() + bird.getWidth() < -60;
        boolean outBottom = bird.getY() + bird.getHeight() < -60;
        boolean outTop = bird.getY() > worldHeight + 60;

        if (outRight || outLeft || outBottom || outTop) {
            if (currentLevel == 2) {
                bird.setIdlePosition(birdStartX, birdStartY);
                bird.setAlwaysVisible(true);
                birdAttackCooldownTimer = 2.5f;
            } else {
                bird.resetPosition(birdStartX, birdStartY);
                bird.setAlwaysVisible(false);
                birdTriggered = false;
            }
        }
    }

    private void pushPlayerBackSafely(float totalDistance, boolean pushToLeft) {
        float stepDistance = 8f;
        int steps = (int) (totalDistance / stepDistance);

        for (int i = 0; i < steps; i++) {
            float previousX = player.getX();

            if (pushToLeft) {
                player.setX(player.getX() - stepDistance);
            } else {
                player.setX(player.getX() + stepDistance);
            }

            keepPlayerInsideWorld();
            if (isPlayerCollidingWithTerrain()) {
                player.setX(previousX);
                break;
            }
        }
    }

    // --- REINICIO Y ESTADO GLOBAL ---

    private void checkPlayerEnergy() {
        if (player.getEnergy() > 0) {
            return;
        }

        loseLifeAndReset();
    }

    private void resetLevelState() {
        player.setX(playerStartX);
        player.setY(playerStartY);
        player.setEnergy(player.getMaxEnergy());

        if (relic != null) {
            relic.setCollected(false);
        }
        if (scoreItem != null) {
            scoreItem.reset();
        }
        if (immunityItem != null) {
            immunityItem.reset();
        }
        if (poisonItem != null) {
            poisonItem.reset();
        }

        supply.reset(supplyStartX, supplyStartY);

        if (bird != null) {
            if (currentLevel == 2) {
                bird.setIdlePosition(birdStartX, birdStartY);
                bird.setAlwaysVisible(true);
            } else {
                bird.resetPosition(birdStartX, birdStartY);
                bird.setAlwaysVisible(false);
            }
        }

        birdTriggered = false;
        birdAttackCooldownTimer = 0;

        friendMessageTriggered = false;
        friendMessageTimer = 0;

        guardianDamageTimer = 0;
        birdDamageTimer = 0;
        supplyCooldownTimer = 0;
        supplyUnavailableMessageTimer = 0;
        supplyHealMessageTimer = 0;
        birdHitMessageTimer = 0;
        immunityMessageTimer = 0;
        poisonTimer = 0;
        poisonMessageTimer = 0;
    }

    private void loseLifeAndReset() {
        player.loseLife();

        if (player.getLives() > 0) {
            resetLevelState();
        }
    }

    private void killPlayerByGuardian() {
        guardianDeathMessageTimer = 2f;

        // Sonido de daño al morir por el oso
        playSound("sounds/hurt.wav");

        loseLifeAndReset();
    }

    public boolean isGameOver() {
        return player.getLives() <= 0;
    }

    public boolean isLevelCompleted() {
        return relic.isCollected();
    }

    // --- UPDATE TIMERS ---

    private void updateGuardianDamageTimer(float delta) {
        if (guardianDamageTimer > 0) {
            guardianDamageTimer -= delta;
        }
    }

    private void updateBirdDamageTimer(float delta) {
        if (birdDamageTimer > 0) {
            birdDamageTimer -= delta;
        }
    }

    private void updateScoreMessageTimer(float delta) {
        if (scoreMessageTimer > 0) {
            scoreMessageTimer -= delta;
        }
    }

    private void updateSupplyCooldownTimer(float delta) {
        if (supplyCooldownTimer > 0) {
            supplyCooldownTimer -= delta;
        }
    }

    public void updateSupplyUnavailableMessageTimer(float delta) {
        if (supplyUnavailableMessageTimer > 0) {
            supplyUnavailableMessageTimer -= delta;
        }
    }

    private void updateSupplyHealMessageTimer(float delta) {
        if (supplyHealMessageTimer > 0) {
            supplyHealMessageTimer -= delta;
        }
    }

    private void updateBirdHitMessageTimer(float delta) {
        if (birdHitMessageTimer > 0) {
            birdHitMessageTimer -= delta;
        }
    }

    private void updateImmunityMessageTimer(float delta) {
        if (immunityMessageTimer > 0) {
            immunityMessageTimer -= delta;
        }
    }

    private void updatePoisonTimer(float delta) {
        if (poisonTimer > 0) {
            poisonTimer -= delta;
        }
    }

    private void updatePoisonMessageTimer(float delta) {
        if (poisonMessageTimer > 0) {
            poisonMessageTimer -= delta;
        }
    }

    private void updateGuardianDeathMessageTimer(float delta) {
        if (guardianDeathMessageTimer > 0) {
            guardianDeathMessageTimer -= delta;
        }
    }

    private void updateSpeedBoostTimer(float delta) {
        if (speedBoostTimer > 0) {
            speedBoostTimer -= delta;
        }
    }

    private void updateBirdAttackCooldownTimer(float delta) {
        if (birdAttackCooldownTimer > 0) {
            birdAttackCooldownTimer -= delta;
        }
    }
    private void updateFriendMessageTimer(float delta) {
        if (friendMessageTimer > 0) {
            friendMessageTimer -= delta;
        }
    }

    // --- GETTERS ---

    public Player getPlayer() {
        return player;
    }

    public Relic getRelic() {
        return relic;
    }

    public ScoreItem getScoreItem() {
        return scoreItem;
    }

    public Supply getSupply() {
        return supply;
    }

    public Guardian getGuardian() {
        return guardian;
    }

    public Bird getBird() {
        return bird;
    }

    public ImmunityItem getImmunityItem() {
        return immunityItem;
    }

    public PoisonItem getPoisonItem() {
        return poisonItem;
    }

    public float getGuardianDamageTimer() {
        return guardianDamageTimer;
    }

    public float getGuardianDeathMessageTimer() {
        return guardianDeathMessageTimer;
    }

    public float getScoreMessageTimer() {
        return scoreMessageTimer;
    }

    public float getSupplyCooldownTimer() {
        return supplyCooldownTimer;
    }

    public float getSupplyUnavailableMessageTimer() {
        return supplyUnavailableMessageTimer;
    }

    public float getSupplyHealMessageTimer() {
        return supplyHealMessageTimer;
    }

    public int getLastSupplyHealAmount() {
        return lastSupplyHealAmount;
    }

    public float getBirdHitMessageTimer() {
        return birdHitMessageTimer;
    }

    public float getImmunityMessageTimer() {
        return immunityMessageTimer;
    }

    public float getPoisonMessageTimer() {
        return poisonMessageTimer;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public float getFriendMessageTimer() {
        return friendMessageTimer;
    }
}
