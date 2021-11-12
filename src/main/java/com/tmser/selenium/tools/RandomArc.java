package com.tmser.selenium.tools;
import com.google.common.collect.Lists;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomArc {

    public static List<Point> randomLine(int startX, int startY, int endX, int endY) {
        //随机产生凹凸个数，2-4
        int arcNum = new Random().nextInt(3) + 2;
        int arcStartX = startX,arcStartY = startY;
        int isLeftInt = new Random().nextInt(2);
        boolean isLeftBoolean = false;
        if(isLeftInt == 1){
            isLeftBoolean = true;
        }

        int yChange = 0;
        List<Point> allPoint = Lists.newArrayList();
        for(int i = 0;i < arcNum;i++){
            int arcEndY;
            int arcEndX;
            if(i == arcNum - 1){
                arcEndY = endY;
                arcEndX = endX;
            }else{
                //随机产生两点直线间的分割点
                yChange += randInt(startY, endY) + Math.abs(endY - startY)/10;
                if(endY > startY){
                    arcEndY = startY + yChange;
                }else{
                    arcEndY = startY - yChange;
                }
                int xChange = (int)(yChange*1.0/Math.abs(endY - startY)*Math.abs(endX - startX));
                if(endX > startX){
                    arcEndX = startX + xChange;
                }else{
                    arcEndX = startX - xChange;
                }

            }

            if(((endX > startX)&&(arcEndX > endX)) || ((endY > startY)&&(arcEndY > endY)) || ((endX < startX)&&(arcEndX < endX)) || ((endY < startY)&&(arcEndY < endY))){
                arcEndX = endX;
                arcEndY = endY;
                allPoint.addAll(setGroup(arcStartX, arcStartY, arcEndX, arcEndY,isLeftBoolean));
                return allPoint;
            }

            allPoint.addAll(setGroup(arcStartX, arcStartY, arcEndX, arcEndY, isLeftBoolean));
            isLeftBoolean = !isLeftBoolean;
            arcStartX = arcEndX;
            arcStartY = arcEndY;
        }

        return allPoint;
    }

    private static int randInt(int startY, int endY) {
        int diff = Math.abs(endY - startY)/2;
        return diff > 0 ? new Random().nextInt(diff): diff;
    }


    //生成一段贝叶斯曲线
    private static List<Point> setGroup(int startX, int startY, int endX, int endY, boolean isLeft){
        Point controlPoint;
        if(isLeft){
            //从左下至右上时，向左凸
            controlPoint = new Point(startX + (endX - startX)/2, endY);
        }else{
            //从左下至右上时，向左凸
            controlPoint = new Point(endX, startY + (endY - startY)/2);
        }

         return bezier(new Point(startX, startY), controlPoint,
                new Point(endX, endY));
    }

    //一段贝塞尔曲线的点的集合
    private static List<Point> bezier(Point startPoint, Point controlPoint, Point endPoint) {
        //steps为一段贝塞尔曲线上的点，点越多越圆滑
        int steps =  new Random().nextInt(20) + 15;
        List<Point> list = new ArrayList<Point>();
        float tStep = 1 / ((float) steps);
        float t = 0f;
        for (int ik = 0; ik <= steps; ik++) {
            int x = (int) calculateQuadSpline(startPoint.x, controlPoint.x, endPoint.x, t);
            int y = (int) calculateQuadSpline(startPoint.y, controlPoint.y, endPoint.y, t);
            list.add(new Point(x, y));
            t = t + tStep;
        }
        return list;
    }

    private static float calculateQuadSpline(float z0, float z1, float z2, float t) {
        float a1 = (float) ((1.0 - t) * (1.0 - t) * z0);
        float a2 = (float) (2.0 * t * (1 - t) * z1);
        float a3 = (float) (t * t * z2);
        float a4 = a1 + a2 + a3;
        return a4;
    }

    public static void main(String[] args) {
        int xoffset = 40, yoffset = 40;
        List<java.awt.Point> points = RandomArc.randomLine(xoffset, yoffset,
                (int) (xoffset + (50 + 150 * Math.random())), 41);
        System.out.println("points: " + points.size());
        for (Point p: points
             ) {
            System.out.println(p.x + " : " + p.y);
        }
    }
}
