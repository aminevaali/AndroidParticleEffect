package com.example.canvasnotcrash;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ParticleView extends View {
    private Random random = new Random();
    private Paint paint = new Paint();
    private List<Particle> particles = new ArrayList<>();
    private int particlesLength = -1;
    int w, h;
    int lineLimit;

    public ParticleView(Context context) {
        super(context);
//        setup();
    }

    public ParticleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
//        setup();
    }

    public ParticleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        setup();
    }

    private void setup() {
        if (isInEditMode())
            return;
        if (particlesLength != -1)
            return;
        w = getWidth();
        h = getHeight();
        lineLimit = w / 4;
        particlesLength = w / 15;
        for (int i = 0; i < particlesLength; i++) {
            particles.add(new Particle());
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isInEditMode())
            return;
        setup();

        paint.setColor(Color.RED);
        paint.setTextSize(50);

        for (int i = 0; i < particlesLength; i++) {
            particles.get(i).draw(canvas);
            for (int j = i + 1; j < particlesLength; j++) {
                if (distance(particles.get(i), particles.get(j)) < lineLimit) {
                    canvas.drawLine(particles.get(i).x, particles.get(i).y,
                            particles.get(j).x, particles.get(j).y, paint);
                }
            }
            particles.get(i).move();
        }

        try {
            Thread.sleep(1);
            invalidate();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    double distance(Particle p1, Particle p2) {
        return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Toast.makeText(getContext(), "" + event.getX() + ", " + event.getY(), Toast.LENGTH_SHORT).show();
        return super.onTouchEvent(event);
    }

    class Particle {
        int size;
        int velocityX;
        int velocityY;
        int x, y;

        public Particle() {
            size = random.nextInt(3) + 5;
            velocityX = (random.nextInt(2) + 1) * randomSign();
            velocityY = (random.nextInt(2) + 1) * randomSign();
            x = random.nextInt(w);
            y = random.nextInt(h);
        }

        public void move() {
            if (x <= 0 || x >= w) {
                velocityX *= -1;
            }

            if (y <= 0 || y >= h) {
                velocityY *= -1;
            }
            x += velocityX;
            y += velocityY;
        }

        private int randomSign() {
            return (random.nextInt(2) == 0 ? -1 : 1);
        }

        public void draw(Canvas canvas) {
            canvas.drawCircle(x, y, size, paint);
        }
    }
}
