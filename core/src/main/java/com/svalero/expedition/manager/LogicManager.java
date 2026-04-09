package com.svalero.expedition.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Graphics;

import com.svalero.expedition.domain.Player;
import com.svalero.expedition.domain.Relic;
import com.svalero.expedition.domain.Supply;
import com.svalero.expedition.domain.Guardian;
import com.svalero.expedition.domain.Deer;

public class LogicManager {

    private static final float PLAYER_START_X = 100;
    private static final float PLAYER_START_Y = 150;
    private static final float SUPPLY_START_X = 36;
    private static final float SUPPLY_START_Y = 100;

    private final Player player;
    private final Relic relic;
    private final Supply supply;
    private final Guardian guardian;
    private final Deer deer;
    private float guardianDamageTimer; // tiempo de espera entre daños consecutivos
    private float deerDamageTimer; //tiempo de espera entre daños consecutivos
    private float previousPlayerX;


    // Ubicación del jugador
    public LogicManager() {
        player = new Player(PLAYER_START_X, PLAYER_START_Y, 150, 100, 3, 0);
        relic = new Relic(600, 220);
        supply = new Supply(SUPPLY_START_X, SUPPLY_START_Y, 0);
        guardian = new Guardian(500, 140, 100, 150, 300);

        // El ciervo empieza fuera de pantalla por la izquierda, pero a la altura visible del borde superior
        deer = new Deer(-60, Gdx.graphics.getHeight() - 36, 260, 320);

        guardianDamageTimer = 0;
        deerDamageTimer = 0;
        previousPlayerX = player.getX();
    }

    public void update(float delta) {
        handleInput(); // handleInput decide la dirección

        // Si no hay impacto, la niña y el oso mantienen comportamiento normal
        if (guardianDamageTimer <= 0) {
            player.update(delta); // mueve a la niña
            guardian.update(delta); // actualiza al oso
        }

        supply.update(delta); // actualiza al acompañante (Frodo)

        moveSupply(delta);
        checkDeerActivation(); // se decide si el ciervo se mueve
        deer.update(delta); // el ciervo se mueve
        resetDeerIfNeeded();

        updateGuardianDamageTimer(delta);
        updateDeerDamageTimer(delta);

        keepPlayerInsideScreen(); // corrige la posición para que no se salga de los bordes
        checkGuardianBarrier(); // impide que la niña cruce la barrera del guardián
        checkRelicCollision(); // comprueba si se ha llegado al premio
        checkSupplyCollision(); // comprueba si Frodo ha alcanzado al personaje
        checkGuardianCollision(); // comprueba si el oso ha alcanzado al personaje
        checkDeerCollision();
        checkPlayerEnergy();

        previousPlayerX = player.getX();
    }

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

        // Llamada supply
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            supply.setCalled(true);
        }
    }

    // Establece límite de pantalla para que el jugador no pueda sobrepasarla
    private void keepPlayerInsideScreen() {
        Graphics graphics = Gdx.graphics;

        if (player.getX() < 0) {
            player.setX(0);
        }
        if (player.getY() < 0) {
            player.setY(0);
        }

        if (player.getX() + player.getWidth() > graphics.getWidth()) {
            player.setX(graphics.getWidth() - player.getWidth());
        }

        if (player.getY() + player.getHeight() > graphics.getHeight()) {
            player.setY(graphics.getHeight() - player.getHeight());
        }
    }

    // --- HUESO ---

    private void checkRelicCollision() {
        if (relic.isCollected()) {
            return;
        }

        if (guardianDamageTimer > 0) {
            return;
        }

        // Comprueba si hay solapamiento horizontal entre el jugador y el premio
        // El jugador no ha pasado por la derecha del premio (player.getX() < relic.getX() + relic.getWidth())
            // y el jugador no está todavía por la izquierda del premio (player.getX() + player.getWidth() > relic.getX()).
        boolean collisionX = player.getX() < relic.getX() + relic.getWidth()
            && player.getX() + player.getWidth() > relic.getX();

        // Comprueba si hay solapamiento vertical entre el jugador y el premio
        boolean collisionY = player.getY() < relic.getY() + relic.getHeight()
            && player.getY() + player.getHeight() > relic.getY();

        if (collisionX && collisionY) {
            relic.setCollected(true);
            player.setScore(player.getScore()+ 100);
        }
    }

    // --- FRODO ---

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
            float followDistance = supply.getFollowDistance();

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

        float speed = 120f;

        supply.setX(supply.getX() + directionX * speed * delta);
        supply.setY(supply.getY() + directionY * speed * delta);
    }

    private void checkSupplyCollision() {
        if (!supply.isCalled()) {
            return;
        }

        boolean collisionX = player.getX() < supply.getX() + supply.getWidth()
            && player.getX() + player.getWidth() > supply.getX();

        boolean collisionY = player.getY() < supply.getY() + supply.getHeight()
            && player.getY() + player.getHeight() > supply.getY();

        if (collisionX && collisionY) {
            player.setEnergy(player.getEnergy() + 25);

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

    private void checkGuardianBarrier() {
        // La barrera solo actúa fuera del espacio patrullado por el guardián
        boolean playerInGuardianRange = player.getY() + player.getHeight() > guardian.getMinY()
            && player.getY() + player.getHeight() < guardian.getMaxY();

        if (!playerInGuardianRange) {
            // Si la niña intenta cruzar la línea del guardián por fuera de su tramo, no se permite
            if (player.getX() + player.getWidth() > guardian.getX()) {
                player.setX(guardian.getX() - player.getWidth());
            }
        }
    }

    private void checkGuardianCollision() {
        boolean collisionX = player.getX() < guardian.getX() + guardian.getWidth()
            && player.getX() + player.getWidth() > guardian.getX();

        boolean collisionY = player.getY() < guardian.getY() + guardian.getHeight()
            && player.getY() + player.getHeight() > guardian.getY();


        if (collisionX && collisionY && guardianDamageTimer <= 0) {
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
        boolean collisionX = player.getX() < deer.getX() + deer.getWidth()
            && player.getX() + player.getWidth() > deer.getX();

        boolean collisionY = player.getY() < deer.getY() + deer.getHeight()
            && player.getY() + player.getHeight() > deer.getY();


        if (collisionX && collisionY && deerDamageTimer <= 0) {
            // el ciervo quita el 50% de la energía disponible
            int damage = player.getEnergy() / 2;
            player.setEnergy(player.getEnergy() - damage);

            // El ciervo también penaliza la puntuación
            player.setScore(player.getScore() - 15);

            // Activa de un segundo tras impacto
            deerDamageTimer = 1f;

            // El ciervo derriba al jugador (niña) hacia atrás
            if (deer.isMovingRight()) {
                player.setX(player.getX() - 64);
            } else {
                player.setX(player.getX() + 64);
            }
            keepPlayerInsideScreen();

            // El acompañante (Frodo) se recoloca automáticamente dos casillas a la derecha de la niña
            supply.setX(player.getX() + 64);
            supply.setY(player.getY());
            supply.setCalled(false);
        }
    }

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

    public Player getPlayer() {
        return player;
    }

    public Relic getRelic() {
        return relic;
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

    public float getGuardianDamageTimer() {
        return guardianDamageTimer;
    }

    public boolean isGameOver() {
        return player.getLives() <= 0;
    }

    public boolean isLevelCompleted() {
        return relic.isCollected();
    }
}
