package com.svalero.expedition.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapLayer;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.svalero.expedition.domain.Player;
import com.svalero.expedition.domain.Relic;
import com.svalero.expedition.domain.Supply;
import com.svalero.expedition.domain.Guardian;
import com.svalero.expedition.domain.Deer;
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

    private static final float IMMUNITY_DURATION = 5f;

    private static final float POISON_DURATION = 7f;
    private static final float POISON_SPEED_FACTOR = 0.2f;

    private final Player player;
    private Relic relic;
    private final Supply supply;
    private Guardian guardian;
    private Deer deer;
    private ScoreItem scoreItem;
    private ImmunityItem immunityItem;
    private PoisonItem poisonItem;


        // timers
    private float guardianDamageTimer; // tiempo de espera entre daños consecutivos.
    private float deerDamageTimer; //tiempo de espera entre daños consecutivos
    private float previousPlayerX;
    private float scoreMessageTimer;
    private float supplyCooldownTimer;
    private float supplyUnavailableMessageTimer; // Mensaje ayuda de Frodo no disponible
    private float supplyHealMessageTimer;
    private int lastSupplyHealAmount;
    private float deerHitMessageTimer;
    private float immunityTimer;
    private float immunityMessageTimer;
    private float poisonTimer;
    private float poisonMessageTimer;

    private float worldWidth;
    private float worldHeight;
    private TiledMapTileLayer collisionLayer;
    private float tileWidth;
    private float tileHeight;


    // Ubicación del jugador
    public LogicManager() {
        player = new Player(PLAYER_START_X, PLAYER_START_Y, 150, 100, 3, 0);
        relic = new Relic (600, 220);
        scoreItem = new ScoreItem(320, 220, 24, 24, 25);
        supply = new Supply(SUPPLY_START_X, SUPPLY_START_Y, 0);
        guardian = new Guardian(500, 140, 100, 150, 300);
        immunityItem = new ImmunityItem(200, 300, 24, 24);
        poisonItem = new PoisonItem(400, 300, 24, 24);
        immunityTimer = 0;
        poisonTimer = 0;
        scoreMessageTimer = 0;
        poisonMessageTimer = 0;

        // El ciervo empieza fuera de pantalla por la izquierda, pero a la altura visible del borde superior
        deer = new Deer(-60, Gdx.graphics.getHeight() - 36, 260, 320);

        guardianDamageTimer = 0;
        deerDamageTimer = 0;
        supplyCooldownTimer = 0;
        supplyUnavailableMessageTimer = 0;
        supplyHealMessageTimer = 0;
        lastSupplyHealAmount = 0;
        deerHitMessageTimer = 0;
        immunityMessageTimer = 0;

        previousPlayerX = player.getX();
        worldWidth = Gdx.graphics.getWidth();
        worldHeight = Gdx.graphics.getHeight();
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

        if(objectsLayer == null) {
            System.err.println("ERROR: La capa 'Objects' es null. Asegúrate de que la capa en Tiled se llama exactamente 'Objects' (sensitive Case).");
            return;
        }

        MapObjects objects = objectsLayer.getObjects();

        for (MapObject object : objects) {

            // Tratando de eliminar problema de incompatibilidad de nombre entre versiones
            String type = (String) object.getProperties().get("type");
            if (type == null) type = (String) object.getProperties().get("class");
            if (type == null) type = object.getName();

            if (type == null) {
                continue;
            }

            Object propX = object.getProperties().get("x");
            Object propY = object.getProperties().get("y");

            if (propX == null || propY == null) {
                continue;
            }

            // también se aplica la escala 2f a las coordenadas de los objetos para un posicionamiento correcto
            float scale = 2f;
            float x = ((Number) propX).floatValue() * scale;
            float y = ((Number) propY).floatValue() * scale;

            switch (type) {

                case "player":
                    player.setX(x);
                    player.setY(y);
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
                    // propiedades de Tiled
                    Object propSpeed = object.getProperties().get("speed");
                    Object propMinY = object.getProperties().get("minY");
                    Object propMaxY = object.getProperties().get("maxY");

                    // conversión de valores number/float
                    float speed = (propSpeed != null) ? ((Number) propSpeed).floatValue() : 100f;

                    // minY y minX tambíen se multiplican por la escala (2f) para garantizar la posición definida
                    float minY = (propMinY != null) ? ((Number) propMinY).floatValue() * scale : 240 * scale;
                    float maxY = (propMaxY != null) ? ((Number) propMaxY).floatValue() * scale : 280 * scale;

                    guardian = new Guardian(x, y, speed, minY, maxY);
                    break;

                case "deer":
                    deer.resetPosition(x, y);
                    break;

                default:
                    System.out.println("Objeto de tipo no reconocido: " + type);
                    break;
            }
        }
    }

    // --- CORE LOOP --- Lógica que se ejecuta continuamente

    public void update(float delta) {
        handleInput(); // handleInput decide la dirección

        // Si no hay impacto, la niña y el oso mantienen comportamiento normal
        if (guardianDamageTimer <= 0) {
            float speedFactor = (poisonTimer > 0) ? POISON_SPEED_FACTOR : 1f;
            player.setSpeedMultiplier(speedFactor);
            movePlayer(delta);
            guardian.update(delta); // actualiza al oso
        }

        supply.update(delta); // actualiza al acompañante (Frodo)

        moveSupply(delta);
        checkDeerActivation(); // se decide si el ciervo se mueve
        deer.update(delta); // el ciervo se mueve
        resetDeerIfNeeded();

        updateGuardianDamageTimer(delta);
        updateDeerDamageTimer(delta);
        updateScoreMessageTimer(delta);
        updateSupplyCooldownTimer(delta);
        updateSupplyUnavailableMessageTimer(delta);
        updateSupplyHealMessageTimer(delta);
        updateDeerHitMessageTimer(delta);
        updateImmunityTimer(delta);
        updateImmunityMessageTimer(delta);
        updatePoisonTimer(delta);
        updatePoisonMessageTimer(delta);

        keepPlayerInsideWorld(); // corrige la posición para que no se salga de los bordes del mapa
        checkRelicCollision(); // comprueba si se ha llegado al premio
        checkScoreItemCollision(); // comprueba si se ha llegado al item
        checkSupplyCollision(); // comprueba si Frodo ha alcanzado al personaje
        checkGuardianCollision(); // comprueba si el oso ha alcanzado al personaje
        checkDeerCollision();
        checkImmunityItemCollision();
        checkPoisonItemCollision();
        checkPlayerEnergy();

        previousPlayerX = player.getX();
    }

    // --- HELPERS --- Médosos de apoyo al bucle principal
        // Ayudan directamente al movimiento y la física básica

    private void handleInput() {

        // Si la niña choca con el oso, no puede moverse ni llamar a Frodo
        if (guardianDamageTimer > 0) {
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
        player.setX(player.getX()); // mantiene X actual
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
            player.setScore(player.getScore()+ 100);
        }
    }

    // --- ITEM SCORE+ ---
    private void updateScoreMessageTimer(float delta) {
        if (scoreMessageTimer > 0) {
            scoreMessageTimer -= delta;
        }
    }

    private void checkScoreItemCollision() {
        if (scoreItem.isCollected()) {
            return;
        }

        if (guardianDamageTimer > 0) {
            return;
        }

        boolean collisionX = getPlayerHitboxLeft() < scoreItem.getX() + scoreItem.getWidth()
            && getPlayerHitboxRight() > scoreItem.getX();

        boolean collisionY = getPlayerHitboxBottom() < scoreItem.getY() + scoreItem.getHeight()
            && getPlayerHitboxTop() > scoreItem.getY();

        if (collisionX && collisionY) {
            scoreItem.setCollected(true);
            player.setScore(player.getScore() + scoreItem.getScoreValue());
            scoreMessageTimer = 1.5f; // muestra la puntuación 1.5s
        }
    }

    // --- ITEM INMUNIDAD ---

    private void updateImmunityTimer(float delta) {
        if (immunityTimer > 0) {
            immunityTimer -= delta;
        }
    }

    private void updateImmunityMessageTimer(float delta) {
        if (immunityMessageTimer > 0) {
            immunityMessageTimer -= delta;
        }
    }

    private void checkImmunityItemCollision() {
        if (immunityItem.isCollected()) {
            return;
        }

        boolean collisionX = getPlayerHitboxLeft() < immunityItem.getX() + immunityItem.getWidth()
            && getPlayerHitboxRight() > immunityItem.getX();

        boolean collisionY = getPlayerHitboxBottom() < immunityItem.getY() + immunityItem.getHeight()
            && getPlayerHitboxTop() > immunityItem.getY();

        if (collisionX && collisionY) {
            immunityItem.setCollected(true);
            immunityTimer = IMMUNITY_DURATION;
            immunityMessageTimer = 1.5f;
        }
    }

    // --- ITEM VENENO ---

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

    private void checkPoisonItemCollision() {
        if (poisonItem.isCollected()) {
            return;
        }

        boolean collisionX = getPlayerHitboxLeft() < poisonItem.getX() + poisonItem.getWidth()
            && getPlayerHitboxRight() > poisonItem.getX();

        boolean collisionY = getPlayerHitboxBottom() < poisonItem.getY() + poisonItem.getHeight()
            && getPlayerHitboxTop() > poisonItem.getY();

        if (collisionX && collisionY) {
            poisonItem.setCollected(true);
            poisonTimer = POISON_DURATION;
            poisonMessageTimer = 1.5f;
        }
    }

    // --- FRODO ---

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
        if (supplyHealMessageTimer > 0 ) {
            supplyHealMessageTimer -= delta;
        }
    }

    private void moveSupply(float delta) {
        float targetX;
        float targetY;

        // Si se llama a Frodo, el objetivo es la posición de la niña
        if (supply.isCalled()) {
            targetX = player.getX();
            targetY = player.getY();
        } else {
            // Posiciones posibles de acompañamiento a 64 px para evitar la sobreposición:
            // izquierda, derecha, abajo y arriba
            float followDistance = supply.getFollowDistance() + SUPPLY_FOLLOW_EXTRA_DISTANCE;

            float[][] candidatePositions = {
                {player.getX() - followDistance, player.getY()}, // izquierda
                {player.getX() + followDistance, player.getY()}, // derecha
                {player.getX(), player.getY() - followDistance}, // abajo
                {player.getX(), player.getY() + followDistance}  // arriba
            };

            int bestIndex = -1;
            float bestDistance = Float.MAX_VALUE;

            for (int i = 0; i < candidatePositions.length; i++) {
                float candidateX = candidatePositions[i][0];
                float candidateY = candidatePositions[i][1];

                boolean validX = candidateX >= 0 && candidateX + supply.getWidth() <= Gdx.graphics.getWidth();
                boolean validY = candidateY >= 0 && candidateY + supply.getHeight() <= Gdx.graphics.getHeight();

                if (!validX || !validY) {
                    continue;
                }

                float distanciaX = candidateX - supply.getX();
                float distanciaY = candidateY - supply.getY();
                float distance = (float) Math.sqrt(distanciaX * distanciaX + distanciaY * distanciaY);

                if (distance < bestDistance) {
                    bestDistance = distance;
                    bestIndex = i;
                }
            }

            if (bestIndex == -1) {
                return;
            }

            targetX = candidatePositions[bestIndex][0];
            targetY = candidatePositions[bestIndex][1];
        }

        float distanciaX = targetX - supply.getX();
        float distanciaY = targetY - supply.getY();

        float distance = (float) Math.sqrt(distanciaX * distanciaX + distanciaY * distanciaY);

        if (distance < 5f) {
            return;
        }

        float directionX = distanciaX / distance;
        float directionY = distanciaY / distance;

        float speed;
        if (supply.isCalled()) {
            speed = SUPPLY_CALL_SPEED;
        } else {
            speed = SUPPLY_FOLLOW_SPEED;
        }

        supply.setX(supply.getX() + directionX * speed * delta);
        supply.setY(supply.getY() + directionY * speed * delta);
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

            // Frodo entra en espera tras curar a Emily
            supplyCooldownTimer = SUPPLY_CALL_COOLDOWN;

            // Frodo deja de estar en modo llamada
            supply.setCalled(false);
        }
    }

    // --- OSO ---

    private void updateGuardianDamageTimer(float delta) {
        if (guardianDamageTimer > 0) {
            guardianDamageTimer -= delta;
        }
    }

    private void checkGuardianCollision() {
        float guardianCollisionWidth = 28f;
        float guardianCollisionHeight = 20f;

        float guardianLeft = guardian.getX() + (guardian.getWidth() - guardianCollisionWidth) / 2f;
        float guardianRight = guardianLeft + guardianCollisionWidth;
        float guardianBottom = guardian.getY();
        float guardianTop = guardianBottom + guardianCollisionHeight;

        boolean collisionX = getPlayerHitboxLeft() < guardianRight
            && getPlayerHitboxRight() > guardianLeft;

        boolean collisionY = getPlayerHitboxBottom() < guardianTop
            && getPlayerHitboxTop() > guardianBottom;

        if (collisionX && collisionY && guardianDamageTimer <= 0 && immunityTimer <= 0) {
            // el oso quita una vida
            player.loseLife();

            // El oso penaliza la puntuación
            player.setScore(player.getScore() - 25);

            // Activa de un segundo tras el impacto
            guardianDamageTimer = 1f;

            // Jugador (niña) vuelve a la salida
            player.setX(PLAYER_START_X);
            player.setY(PLAYER_START_Y);

            // Acompañante (Frodo) también vuelve a la salida
            supply.setX(SUPPLY_START_X);
            supply.setY(SUPPLY_START_Y);
            supply.setCalled(false);
        }
    }

    // --- CIERVO ---

    private void updateDeerDamageTimer(float delta) {
        if (deerDamageTimer > 0) {
            deerDamageTimer -= delta;
        }
    }

    // Detecta si la niña pasa por el punto de activación
    private void checkDeerActivation() {
        if (deer.isActive()) {
            return;
        }

        float playerCenterX = player.getX() + player.getWidth() / 2;
        float playerCenterY = player.getY() + player.getHeight() / 2;

        // La niña cruza el punto de activación hacia la derecha
        if (previousPlayerX <= deer.getTriggerX() && player.getX() > deer.getTriggerX()) {
            deer.resetPosition(-60, Gdx.graphics.getHeight() - deer.getHeight());
            deer.activateTowards(playerCenterX, playerCenterY);
        }

        // La niña cruza el punto de activación hacia la izquierda
        if (previousPlayerX >= deer.getTriggerX() && player.getX() < deer.getTriggerX()) {
            deer.resetPosition(Gdx.graphics.getWidth() + 60, 0);
            deer.activateTowards(playerCenterX, playerCenterY);
        }
    }

    // Coloca al ciervo fuera de la pantalla al terminar
    private void resetDeerIfNeeded() {
        if (!deer.isActive()) {
            return;
        }

        // El ciervo no debe invadir la zona final del guardián.
        // Si llega a esa parte del escenario, se resetea para poder volver a activarse.
        float deerLimitRight = guardian.getX() - deer.getWidth() - 20;
        float deerLimitLeft = deer.getTriggerX() + 20;

        if (deer.isMovingRight()) {
            boolean outRight = deer.getX() > Gdx.graphics.getWidth();
            boolean outBottom = deer.getY() + deer.getHeight() < 0;
            boolean reachedGuardianZone = deer.getX() > deerLimitRight;

            if (outRight || outBottom || reachedGuardianZone) {
                deer.resetPosition(-60, Gdx.graphics.getHeight() - deer.getHeight());
            }
        } else {
            boolean outLeft = deer.getX() + deer.getWidth() < 0;
            boolean outTop = deer.getY() > Gdx.graphics.getHeight();
            boolean crossedBackTooMuch = deer.getX() < deerLimitLeft;

            if (outLeft || outTop || crossedBackTooMuch) {
                deer.resetPosition(Gdx.graphics.getWidth() + 60, 0);
            }
        }
    }

    // Colisión. Se bloquea 1'' y se desplaza a la niña dos casillas atrás
    private void checkDeerCollision() {
        float deerCollisionWidth = 28f;
        float deerCollisionHeight = 20f;

        float deerLeft = deer.getX() + (deer.getWidth() - deerCollisionWidth) / 2f;
        float deerRight = deerLeft + deerCollisionWidth;
        float deerBottom = deer.getY();
        float deerTop = deerBottom + deerCollisionHeight;

        boolean collisionX = getPlayerHitboxLeft() < deerRight
            && getPlayerHitboxRight() > deerLeft;

        boolean collisionY = getPlayerHitboxBottom() < deerTop
            && getPlayerHitboxTop() > deerBottom;

        if (collisionX && collisionY && deerDamageTimer <= 0 && immunityTimer <=0) {
            // el ciervo quita el 50% de la energía disponible
            int damage = player.getEnergy() / 2;
            player.setEnergy(player.getEnergy() - damage);

            // El ciervo también penaliza la puntuación
            player.setScore(player.getScore() - 15);

            // Activa de un segundo tras impacto
            deerDamageTimer = 1f;
            deerHitMessageTimer = 1.5f;

            // El ciervo derriba al jugador (niña) hacia atrás, evitando los obstáculos
            if (deer.isMovingRight()) {
                pushPlayerBackSafely(64f, true);
            } else {
                pushPlayerBackSafely(64f, false);
            }

            keepPlayerInsideWorld();

            // El acompañante (Frodo) se recoloca automáticamente dos casillas a la derecha de la niña
            supply.setX(player.getX() + 64);
            supply.setY(player.getY());
            supply.setCalled(false);
        }
    }

    private void pushPlayerBackSafely(float totalDistance, boolean pushToleft) {
        float stepDistance = 8f;
        int steps = (int) (totalDistance / stepDistance);

        for (int i = 0; i < steps; i++) {
            float previousX = player.getX();

            if (pushToleft) {
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

    private void updateDeerHitMessageTimer(float delta) {
        if (deerHitMessageTimer > 0) {
            deerHitMessageTimer -= delta;
        }
    }

    // --- COMPROBACIONES DE ESTADO GLOBAL ---

    private void checkPlayerEnergy() {
        if (player.getEnergy() > 0) {
            return;
        }

        // pierde una vida
        player.loseLife();

        // recupera toda la energía
        player.setEnergy(player.getMaxEnergy());

        // Se reinicia la posición del jugador
        player.setX(PLAYER_START_X);
        player.setY(PLAYER_START_Y);

        // Se reinicia la posición de Frodo
        supply.setX(SUPPLY_START_X);
        supply.setY(SUPPLY_START_Y);
        supply.setCalled(false);
    }

    public boolean isGameOver() {
        return player.getLives() <= 0;
    }

    public boolean isLevelCompleted() {
        return relic.isCollected();
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

    public Deer getDeer() {
        return deer;
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

    public float getDeerHitMessageTimer() {
        return deerHitMessageTimer;
    }

    public float getImmunityMessageTimer() {
        return immunityMessageTimer;
    }

    public float getPoisonMessageTimer() {
        return poisonMessageTimer;
    }
}
