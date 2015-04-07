package a3.kmap165Engine.custom_objects;

import sage.scene.shape.*;
import sage.scene.TriMesh;
import com.jogamp.common.nio.Buffers;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class MyDiamond extends TriMesh{
   private static float[] vrts = new float[] {0.0f, 2.0f, 0.0f, 
      -1.0f, 0.0f, -1.0f, -1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, -1.0f,
      0.0f, -2.0f, 0.0f};
   private static float[] cl = new float[] {1.0f, 1.0f, 1.0f, 1.0f, 
      1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.5f, 0.5f, 0.5f, 1.0f,
      0.0f, 0.0f, 0.0f, 1.0f};
   private static int[] triangles = new int[] {0,1,2, 0,2,3, 0,3,4, 0,4,1, 
      5,1,2, 5,2,3, 5,3,4, 5,4,1};
   public MyDiamond(){ 
      int i;
      FloatBuffer vertBuf =
         com.jogamp.common.nio.Buffers.newDirectFloatBuffer(vrts);
      FloatBuffer colorBuf =
         com.jogamp.common.nio.Buffers.newDirectFloatBuffer(cl);
      IntBuffer triangleBuf =
         com.jogamp.common.nio.Buffers.newDirectIntBuffer(triangles);
      this.setVertexBuffer(vertBuf);
      this.setColorBuffer(colorBuf);
      this.setIndexBuffer(triangleBuf); 
   } 
}