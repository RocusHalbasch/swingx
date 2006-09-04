/*
 * $Id$
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package org.jdesktop.swingx.painter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JComponent;
import org.jdesktop.swingx.util.Resize;
import org.joshy.util.u;

/**
 * <p>A Painter that paints Shapes. It uses a stroke and a fillPaint to do so. The
 * shape is painted as is, at a specific location. If no Shape is specified, nothing
 * will be painted. If no stroke is specified, the default for the Graphics2D
 * will be used. If no fillPaint is specified, the component background color
 * will be used. And if no location is specified, then the shape will be draw
 * at the origin (0,0)</p>
 *
 * <p>Here is an example that draws a lowly rectangle:
 * <pre><code>
 *  Rectangle2D.Double rect = new Rectangle2D.Double(0, 0, 50, 50);
 *  ShapePainter p = new ShapePainter(rect);
 *  p.setLocation(new Point2D.Double(20, 10));
 * </code></pre>
 *
 *
 * @author rbair
 */
public class ShapePainter extends PositionedPainter {
    
    /**
     * The Shape to fillPaint. If null, nothing is painted.
     */
    private Shape shape;
    /**
     * The Stroke to use when painting. If null, the default Stroke for
     * the Graphics2D is used
     */
    private int stroke;
    /**
     * The Paint to use when painting the shape. If null, then the component
     * background color is used
     */
    private Paint fillPaint;
    /**
     * The Paint to use when stroking the shape (drawing the outline). If null,
     * then the component foreground color is used
     */
    private Paint strokePaint;
    
    /**
     * Indicates whether the shape should be filled or outlined, or both
     */
    private Style style = Style.BOTH;
    
    
    private boolean snapPaint;
    
    /**
     * Create a new ShapePainter
     */
    public ShapePainter() {
        super();
        this.shape = new Ellipse2D.Double(0,0,100,100);
        this.stroke = 3;
        this.fillPaint = Color.RED;
        this.strokePaint = Color.BLACK;
    }
    
    /**
     * Create a new ShapePainter with the specified shape.
     *
     *
     * @param shape the shape to fillPaint
     */
    public ShapePainter(Shape shape) {
        super();
        this.shape = shape;
    }
    
    /**
     * Create a new ShapePainter with the specified shape and fillPaint.
     *
     *
     * @param shape the shape to fillPaint
     * @param paint the fillPaint to be used to fillPaint the shape
     */
    public ShapePainter(Shape shape, Paint paint) {
        super();
        this.shape = shape;
        this.fillPaint = paint;
    }
    
    /**
     * Create a new ShapePainter with the specified shape and fillPaint. The shape
     * can be filled or stroked (only the ouline is painted).
     *
     *
     * @param shape the shape to fillPaint
     * @param paint the fillPaint to be used to fillPaint the shape
     * @param style specifies the ShapePainter.Style to use for painting this shape.
     *        If null, then Style.BOTH is used
     */
    public ShapePainter(Shape shape, Paint paint, Style style) {
        super();
        this.shape = shape;
        this.fillPaint = paint;
        this.style = style == null ? Style.BOTH : style;
    }
    
    /**
     * Sets the shape to fillPaint. This shape is not resized when the component
     * bounds are. To do that, create a custom shape that is bound to the
     * component width/height
     *
     *
     * @param s the Shape to fillPaint. May be null
     */
    public void setShape(Shape s) {
        Shape old = getShape();
        this.shape = s;
        firePropertyChange("shape", old, getShape());
    }
    
    /**
     *
     *
     * @return the Shape to fillPaint. May be null
     */
    public Shape getShape() {
        return shape;
    }
    
    /**
     * Sets the stroke to use for painting. If null, then the default Graphics2D
     * stroke use used
     *
     *
     * @param s the Stroke to fillPaint with
     */
    public void setStrokeWidth(int s) {
        int old = getStrokeWidth();
        this.stroke = s;
        firePropertyChange("strokeWidth", old, getStrokeWidth());
    }
    
    /**
     * @return the Stroke to use for painting
     */
    public int getStrokeWidth() {
        return stroke;
    }
    
    /**
     * The Paint to use for filling the shape. Can be a Color, GradientPaint,
     * TexturePaint, or any other kind of Paint. If null, the component
     * background is used.
     *
     * @param p the Paint to use for painting the shape. May be null.
     */
    public void setFillPaint(Paint p) {
        Paint old = getFillPaint();
        this.fillPaint = p;
        firePropertyChange("fillPaint", old, getFillPaint());
    }
    
    /**
     * @return the Paint used when painting the shape. May be null
     */
    public Paint getFillPaint() {
        return fillPaint;
    }
    
    /**
     * The Paint to use for stroking the shape (painting the outline).
     * Can be a Color, GradientPaint, TexturePaint, or any other kind of Paint.
     * If null, the component foreground is used.
     *
     * @param p the Paint to use for stroking the shape. May be null.
     */
    public void setStrokePaint(Paint p) {
        Paint old = getStrokePaint();
        this.strokePaint = p;
        firePropertyChange("strokePaint", old, getStrokePaint());
    }
    
    /**
     * @return the Paint used when stroking the shape. May be null
     */
    public Paint getStrokePaint() {
        return strokePaint;
    }
    
    
    
    /**
     * The shape can be filled or simply stroked (outlined), or both. By default,
     * the shape is both filled and stroked. This property specifies the strategy to
     * use.
     *
     * @param s the Style to use. If null, Style.BOTH is used
     */
    public void setStyle(Style s) {
        Style old = getStyle();
        this.style = s == null ? Style.BOTH : s;
        firePropertyChange("style", old, getStyle());
    }
    
    /**
     * @return the Style used
     */
    public Style getStyle() {
        return style;
    }
    
    public boolean isSnapPaint() {
        return snapPaint;
    }
    
    public void setSnapPaint(boolean snapPaint) {
        boolean old = this.isSnapPaint();
        this.snapPaint = snapPaint;
        firePropertyChange("snapPaint",old,this.snapPaint);
    }
    
    /**
     * @inheritDoc
     */
    public void paintBackground(Graphics2D g, JComponent component, int w, int h) {
        //set the stroke if it is not null
        Stroke s = new BasicStroke(getStrokeWidth());
        g.setStroke(s);
        
        if(getShape() != null) {
            Shape shape = getShape();
            Rectangle bounds = shape.getBounds();
            Rectangle rect = calculatePosition(bounds.width, bounds.height, w, h);
            u.p("rect = " + rect);
            g = (Graphics2D)g.create();
            g.translate(rect.x, rect.y);
            //draw/fill the shape
            switch (getStyle()) {
                case BOTH:
                    drawShape(g, shape, component, rect.width, rect.height);
                    fillShape(g, shape, component, rect.width, rect.height);
                    break;
                case FILLED:
                    fillShape(g, shape, component, rect.width, rect.height);
                    break;
                case OUTLINE:
                    drawShape(g, shape, component, rect.width, rect.height);
                    break;
            }
            
            
            g.dispose();
            return;
        }
    }
    
    private void drawShape(Graphics2D g, Shape shape, JComponent component, int w, int h) {
        g.setPaint(calculateStrokePaint(component, w, h));
        g.draw(shape);
    }
    
    private void fillShape(Graphics2D g, Shape shape, JComponent component, int w, int h) {
        if(shapeEffect != null) {
            Paint pt = calculateFillPaint(component, w, h);
            if(!(pt instanceof Color)) {
                pt = Color.BLUE;
            }
            shapeEffect.apply(g, shape, w, h, (Color)pt);
        } else {
            g.setPaint(calculateFillPaint(component, w, h));
            g.fill(shape);
        }
    }
    
    // shape effect stuff
    public Shape provideShape() {
        return getShape();
    }
    
    private ShapeEffect shapeEffect;
    public void setShapeEffect(ShapeEffect shapeEffect) {
        this.shapeEffect = shapeEffect;
    }
    
    public ShapeEffect getShapeEffect() {
        return this.shapeEffect;
    }
    
    
    
    private Paint calculateStrokePaint(JComponent component, int width, int height) {
        Paint p = getStrokePaint();
        if (p == null) {
            p = component.getForeground();
        }
        if(isSnapPaint()) {
            p = AreaPainter.calculateSnappedPaint(p, width, height);
        }
        return p;
    }
    
    private Paint calculateFillPaint(JComponent component, int width, int height) {
        //set the fillPaint
        Paint p = getFillPaint();
        if(isSnapPaint()) {
            u.p("snapping " + width + " " + height);
            p = AreaPainter.calculateSnappedPaint(p, width, height);
        } else {
            u.p("not snapping");
        }
        if (p == null) {
            p = component.getBackground();
        }
        return p;
    }
}
