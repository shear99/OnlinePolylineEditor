package polyline_editor;

// java.awt 의 Point 와 이름이 겹쳐서 기존의 Point 클래스의 이름을 PolyPoint 로 변경했습니다
public class PolyPoint {
    private double posX, posY;
    private boolean moveAble = true;
    private boolean edgePoint = false;

    public PolyPoint(double x, double y) {
        posX = x;
        posY = y;
    }

    public double getX() {
        return posX;
    }
    public double getY() {
        return posY;
    }

    public void setX(double x) {
        posX = x;
    }
    public void setY(double y) {
        posY = y;
    }

    public boolean getMoveAble() {
        return moveAble;
    }
    public void setMoveAble(boolean m) {
        moveAble = m;
    }

    public boolean getEgdePoint(){ return edgePoint;}
    public void setEdgePoint(boolean e) {edgePoint = e;}

}
