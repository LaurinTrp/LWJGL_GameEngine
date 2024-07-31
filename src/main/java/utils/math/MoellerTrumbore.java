package main.java.utils.math;

import java.util.Arrays;

import glm.vec._3.Vec3;
import glm.vec._4.Vec4;

public class MoellerTrumbore {

    private static final float EPSILON = 0.0000001f;


    /**
     * Comupute the intersection between a ray and a triangle
     *
     * @param rayOrigin
     * @param rayDirection (must be unit vector!)
     * @param triangle (normal not required)
     * @return - returns a float[4]. [0]-[2] = intersection corrdinates x,y,z,
     * float[3] = distance from ray origin. If not intersection exists, "null"
     * will be returned.
     */
    public Vec4 intersect(
            Vec3 rayOrigin, Vec3 rayDirection,
            Vec3[] triangle) {

        float[] rayOriginArr = rayOrigin.toFa_();
        float[] rayDirectionArr = rayDirection.toFa_();
        
        float[] p1, p2, p3;
        float[] edge1, edge2, h, s, q;
        float a, f, u, v, t;

        // Triangle data
        p1 = new float[] {triangle[0].x,triangle[0].y,triangle[0].z};
        p2 = new float[] {triangle[1].x,triangle[1].y,triangle[1].z};
        p3 = new float[] {triangle[2].x,triangle[2].y,triangle[2].z};
//        n = new float[]{triangle[9], triangle[10], triangle[11]};

        edge1 = SUB(p2, p1);
        edge2 = SUB(p3, p1);
        h = CROSS(rayDirectionArr, edge2);
        a = DOT(edge1, h);

        if (a > -EPSILON && a < EPSILON) {
            return null;    // This ray is parallel to this triangle.
        }

        f = 1.0f / a;
        s = SUB(rayOriginArr, p1);
        u = f * DOT(s, h);

        if (u < 0.0 || u > 1.0) {
            return null;
        }

        q = CROSS(s, edge1);
        v = f * DOT(rayDirectionArr, q);

        if (v < 0.0 || u + v > 1.0) {
            return null;
        }

        t = f * DOT(edge2, q);
        if (t > EPSILON) // ray intersection
        {
            return new Vec4(new float[]{
                rayOriginArr[0] + rayDirectionArr[0] * t,
                rayOriginArr[1] + rayDirectionArr[1] * t,
                rayOriginArr[2] + rayDirectionArr[2] * t,
                t}, 0);
        }

        return null;
    }

    /**
     * subtract
     *
     * @param p1
     * @param p2
     * @return
     */
    private float[] SUB(float[] p1, float[] p2) {
        return new float[]{p1[0] - p2[0], p1[1] - p2[1], p1[2] - p2[2]};
    }

    /**
     * Cross product
     *
     * @param p1
     * @param p2
     * @return
     */
    private float[] CROSS(float[] p1, float[] p2) {
        return new float[]{
            p1[1] * p2[2] - p1[2] * p2[1],
            p1[2] * p2[0] - p1[0] * p2[2],
            p1[0] * p2[1] - p1[1] * p2[0]
        };
    }

    /**
     * Dot product
     *
     * @param p1
     * @param p2
     * @return
     */
    private float DOT(float[] p1, float[] p2) {
        return p1[0] * p2[0] + p1[1] * p2[1] + p1[2] * p2[2];
    }

    public static void main(String[] args) {
        MoellerTrumbore mt = new MoellerTrumbore();

        Vec3 origin = new Vec3(10, 10, -1);
        Vec3 direction = new Vec3(0, 0, 1);
        Vec3[] triangle = {new Vec3(0, 0, 0), new Vec3(100, 0, 0), new Vec3(0, 100, 0), new Vec3(0, 0, 1)};

        long s = System.currentTimeMillis();
        Vec4 intersection = null;
        for (int i = 0; i < 5_000_000; i++) {
            intersection = mt.intersect(origin, direction, triangle);
        }
        long e = System.currentTimeMillis();
        System.out.println("TIME: " + (e - s) + "ms");

        if (intersection == null) {
            System.out.println("No intersection.");
        } else {
            System.out.println("Intersection: " + intersection);
        }
    }
}
