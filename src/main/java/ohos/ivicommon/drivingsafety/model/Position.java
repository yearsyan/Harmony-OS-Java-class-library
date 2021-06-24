package ohos.ivicommon.drivingsafety.model;

import java.util.regex.Pattern;

public class Position implements Comparable<Position> {
    private static final int DEFAULT_POS_VALUE = -1;
    private static final Pattern POSITION_REG_PATTERN = Pattern.compile("^(\\+)?\\d+(\\.\\d+)?+(,(\\+)?\\d+(\\.\\d+)?)*$");
    private static final int POS_H = 3;
    private static final int POS_LENGTH = 4;
    private static final int POS_W = 2;
    private static final int POS_X = 0;
    private static final int POS_Y = 1;
    private int height;
    private int width;
    private int xPos;
    private int yPos;

    public Position() {
        setPositionValue(-1, -1, -1, -1);
    }

    public Position(StringBuilder sb) {
        if (sb != null) {
            parsePosition(sb);
        } else {
            setPositionValue(-1, -1, -1, -1);
        }
    }

    public Position(int i, int i2, int i3, int i4) {
        this.xPos = i;
        this.yPos = i2;
        this.height = i3;
        this.width = i4;
    }

    public void setX(int i) {
        this.xPos = i;
    }

    public void setY(int i) {
        this.yPos = i;
    }

    public void setHeight(int i) {
        this.height = i;
    }

    public void setWidth(int i) {
        this.width = i;
    }

    public int getX() {
        return this.xPos;
    }

    public int getY() {
        return this.yPos;
    }

    public int getHeight() {
        return this.height;
    }

    public int getWidth() {
        return this.width;
    }

    private void parsePosition(StringBuilder sb) {
        String sb2 = sb.toString();
        if (!POSITION_REG_PATTERN.matcher(sb2).matches()) {
            setPositionValue(-1, -1, -1, -1);
            return;
        }
        String[] split = sb2.split(",");
        if (split.length != 4) {
            setPositionValue(-1, -1, -1, -1);
        } else {
            setPositionValue(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3]));
        }
    }

    public int compareTo(Position position) {
        if (this == position) {
            return 0;
        }
        if (position == null) {
            return -1;
        }
        if ((position.getX() > 0 || position.getY() > 0 || position.getHeight() > 0 || position.getWidth() > 0) && getX() + position.getX() + position.getWidth() <= getWidth() && getY() + position.getY() + position.getHeight() <= getHeight()) {
            return 1;
        }
        return -1;
    }

    private void setPositionValue(int i, int i2, int i3, int i4) {
        setX(i);
        setY(i2);
        setWidth(i3);
        setHeight(i4);
    }
}
