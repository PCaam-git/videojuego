package com.svalero.expedition.domain;

public class Guardian extends NPC{

    private enum JumpState {
        WAITING,
        JUMPING,
        LANDING
    }

    private static final float LANDING_DURATION = 0.15f;

    private float width;
    private float height;

    // NIVEL 1: patrulla vertical
    private float minY;
    private float maxY;
    private boolean movingUp;
    private boolean usesVerticalPatrol;

    // NIVEL 2: patrulla diagonal con salto
    private boolean usesJumpPatrol;
    private float pointAX;
    private float pointAY;
    private float pointBX;
    private float pointBY;
    private float pointCX;
    private float pointCY;
    private int currentPointIndex;
    private int direction;
    private float waitTime;
    private float waitTimer;
    private float landingTimer;
    private float targetX;
    private float targetY;
    private JumpState jumpState;

    // Constructor nivel 1: patrulla vertical
    public Guardian(float x, float y, float speed, float minY, float maxY) {
        super (x, y, speed);
        this.width = 40;
        this.height = 56;
        this.minY = minY;
        this.maxY = maxY;
        this.movingUp = true;
        this.usesVerticalPatrol = true;
        this.usesJumpPatrol = false;
    }

    // constructor nivel 2: salto entre 3 puntos
    public Guardian(float x, float y, float speed, float pointBX, float pointBY, float pointCX, float pointCY, float waitTime) {
        super(x, y, speed);
        this.width = 40;
        this.height = 56;

        this.pointAX = x;
        this.pointAY = y;
        this.pointBX = pointBX;
        this.pointBY = pointBY;
        this.pointCX = pointCX;
        this.pointCY = pointCY;

        this.currentPointIndex = 0; // empieza en A
        this.direction = 1; // A -> B -> C
        this.waitTime = waitTime;
        this.waitTimer = waitTime;
        this.landingTimer = 0f;
        this.jumpState = JumpState.WAITING;

        this.usesVerticalPatrol = false;
        this.usesJumpPatrol = true;
    }

    @Override
    public void update(float delta) {
        if (usesJumpPatrol) {
            updateJumpPatrol(delta);
        } else {
            updateVerticalPatrol(delta);
        }
    }

    private void updateVerticalPatrol(float delta) {
        if (movingUp) {
            y += speed * delta;

            if (y >= maxY) {
                y = maxY;
                movingUp = false;
            }
        } else {
            y -= speed * delta;

            if (y <= minY) {
                y = minY;
                movingUp = true;
            }
        }
    }

    private void updateJumpPatrol(float delta) {
        switch (jumpState) {
            case WAITING:
                waitTimer -= delta;

                if (waitTimer <= 0) {
                    int nextPointIndex = currentPointIndex + direction;

                    if (nextPointIndex > 2) {
                        direction = -1;
                        nextPointIndex = currentPointIndex + direction;
                    } else if (nextPointIndex < 0) {
                        direction = 1;
                        nextPointIndex = currentPointIndex + direction;
                    }

                    setTargetPoint(nextPointIndex);
                    jumpState = JumpState.JUMPING;
                }
                break;

            case JUMPING:
                float distanceX = targetX - x;
                float distanceY = targetY - y;
                float distance = (float) Math.sqrt(distanceX * distanceX + distanceY * distanceY);

                if (distance <= speed * delta) {
                    x = targetX;
                    y = targetY;
                    currentPointIndex = getTargetPointIndex();
                    landingTimer = LANDING_DURATION;
                    jumpState = JumpState.LANDING;
                    return;
                }

                if (distance > 0) {
                    x += (distanceX / distance) * speed * delta;
                    y += (distanceY / distance) * speed * delta;
                }
                break;

            case LANDING:
                landingTimer -= delta;

                if (landingTimer <= 0) {
                    waitTimer = waitTime;
                    jumpState = JumpState.WAITING;
                }
                break;
        }
    }

    private void setTargetPoint(int pointIndex) {
        if (pointIndex == 1) {
            targetX = pointBX;
            targetY = pointBY;
        } else if (pointIndex == 2) {
            targetX = pointCX;
            targetY = pointCY;
        } else {
            targetX = pointAX;
            targetY = pointAY;
        }
    }

    private int getTargetPointIndex() {
        if (targetX == pointBX && targetY == pointBY) {
            return 1;
        }
        if (targetX == pointCX && targetY == pointCY) {
            return 2;
        }
        return 0;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getMinY() {
        return minY;
    }

    public float getMaxY() {
        return maxY;
    }
}
