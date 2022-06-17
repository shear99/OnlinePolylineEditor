package polyline_editor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

// 폴리라인의 정보를 가지고 있는 클래스
// 마우스 이벤트를 처리하는 MouseAdapter 의 subclass 정의
public class Polyline extends MouseAdapter {
    private ArrayList<PolyPoint> mPts = new ArrayList<PolyPoint>();     // 폴리라인을 구성하는 점들을 ArrayList 로 저장
    private int distanceIndex;
    private boolean moved = false;
    private int startPoint = 0;
    private PolylineEditor.Broadcaster caster;

    public Polyline(PolylineEditor.Broadcaster c) {
        caster = c;
    }

    public void clear() {
        startPoint = 0;
        mPts.clear();
    }

    public int getNumPts() {
        return mPts.size();
    }

    public PolyPoint getPoint(int i) {
        return mPts.get(i);
    }

    public void closedPoint() {
        mPts.add(mPts.get(startPoint));
        for (int i = 0; i < mPts.size(); i++) {
            getPoint(i).setMoveAble(false);
        }
        PolyPoint epoint = new PolyPoint(0, 0);
        epoint.setEdgePoint(true);
        epoint.setMoveAble(false);
        mPts.add(epoint);
        startPoint = getNumPts();
    }

    public int getDistanceIndex(double x, double y) {
        double min = 1000;
        int minIndex = -1;

        for (int i = 0; i < getNumPts(); i++) {
            double dx = Math.abs(x - getPoint(i).getX());
            double dy = Math.abs(y - getPoint(i).getY());

            if (dx + dy < min) {
                min = dx + dy;
                minIndex = i;
            }
        }
        if (min < 10) {
            return minIndex;
        }
        return -1;
    }

    public boolean executeCommand(String cmd) {
        String[] tokens = cmd.split(" ");
        if (tokens[0].equals("add")) {
            distanceIndex = getDistanceIndex(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
            if (distanceIndex == -1) {                  // 거리가 너무 가까우면 드래그 하기 위해 클릭한 것으로 간주하고 새로운 point 생성하지 않음
                moved = false;
                mPts.add(new PolyPoint(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2])));
            } else {
                moved = true;
            }
            return true;
        }
        else if (tokens[0].equals("change")) {
            if(getPoint(distanceIndex).getMoveAble()) {
                mPts.set(distanceIndex, new PolyPoint(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2])));
                System.out.println(Integer.parseInt(tokens[1]) + Integer.parseInt(tokens[2]));
                return true;
            }
        }
        return false;
    }

    public void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        distanceIndex = getDistanceIndex(x, y);
        if (distanceIndex == -1) {                  // 거리가 너무 가까우면 드래그 하기 위해 클릭한 것으로 간주하고 새로운 point 생성하지 않음
            moved = false;
            mPts.add(new PolyPoint(x, y));
        } else {
            moved = true;
        }
        ((JPanel) e.getSource()).repaint();
        caster.broadcastCommand("add " + (int)x +" "+ (int)y);
    }

    public void mouseDragged(MouseEvent e) {

        double x = e.getX();
        double y = e.getY();

        if (moved && getPoint(distanceIndex).getMoveAble()) {
            mPts.set(distanceIndex, new PolyPoint(x, y));
        }
        ((JPanel) e.getSource()).repaint();
        caster.broadcastCommand("change " + (int)x +" "+ (int)y);

    }
}
