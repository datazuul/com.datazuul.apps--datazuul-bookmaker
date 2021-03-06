/**
 *  Path.java
 *
Copyright (c) 2007, 2008, 2009, 2010 Innovatics Inc.

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted provided that the following conditions are met:

    * Redistributions of source code must retain the above copyright notice,
      this list of conditions and the following disclaimer.
 
    * Redistributions in binary form must reproduce the above copyright notice,
      this list of conditions and the following disclaimer in the documentation
      and / or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package com.pdfjet;

import java.util.ArrayList;
import java.util.List;


//>>>>pdfjet {
public class Path {

    private double[] color = {0.0, 0.0, 0.0};
    private double width = 0.3;
    private String pattern = "[] 0";
    private boolean fill_shape = false;
    private boolean close_path = false;

    private List<Point> points = null;

    private double box_x = 0.0;
    private double box_y = 0.0;

    private int lineCapStyle = 0;
    private int lineJoinStyle = 0;


    public Path() {
        points = new ArrayList<Point>();
    }


    public void add(Point point) {
        points.add(point);
    }


    public void setPattern(String pattern) {
        this.pattern = pattern;
    }


    public void setWidth(double width) {
        this.width = width;
    }


    public void setColor(double[] color) {
        this.color = color;
    }


    public void setColor(int[] rgb) {
        this.color = new double[] {rgb[0]/255.0, rgb[1]/255.0, rgb[2]/255.0};
    }


    public void setClosePath(boolean close_path) {
        this.close_path = close_path;
    }


    public void setFillShape(boolean fill_shape) {
        this.fill_shape = fill_shape;
    }


    public void setLineCapStyle(int style) {
        this.lineCapStyle = style;
    }


    public int getLineCapStyle() {
        return this.lineCapStyle;
    }


    public void setLineJoinStyle(int style) {
        this.lineJoinStyle = style;
    }


    public int getLineJoinStyle() {
        return this.lineJoinStyle;
    }


    public void placeIn(
            Box box,
            double x_offset,
            double y_offset) throws Exception {
        box_x = box.x + x_offset;
        box_y = box.y + y_offset;
    }


    public void scaleBy(double factor) throws Exception {
        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            point.x *= factor;
            point.y *= factor;
        }
    }


    public void drawOn(Page page) throws Exception {
        if (fill_shape) {
            page.setBrushColor(
                    color[0], color[1], color[2]);
        } else {
            page.setPenColor(
                    color[0], color[1], color[2]);
        }
        page.setPenWidth(width);
        page.setLinePattern(pattern);
        page.setLineCapStyle(lineCapStyle);
        page.setLineJoinStyle(lineJoinStyle);

        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            point.x += box_x;
            point.y += box_y;
        }

        if (fill_shape) {
            page.drawPath(points, 'f');
        } else {
            if (close_path) {
                page.drawPath(points, 's');
            } else {
                page.drawPath(points, 'S');
            }
        }

        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            point.x -= box_x;
            point.y -= box_y;
        }
    }

}   // End of Path.java
//<<<<}
