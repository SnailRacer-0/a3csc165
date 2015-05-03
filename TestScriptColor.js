var JavaPackages = new JavaImporter(
 Packages.sage.scene.Group,
 Packages.sage.scene.Teapot,
 Packages.java.awt.Color,
 Packages.sage.scene.shape.Line,
 Packages.graphicslib3D.Point3D);
with (JavaPackages)
{
 var lineNodes = new Group();
 
 var origin = new Point3D(0, 0, 0);
 var xEnd = new Point3D(255, 0, 0);
 var yEnd = new Point3D(0, 255, 0);
 var zEnd = new Point3D(0, 0, 255);
 var xAxis = new Line (origin, xEnd, Color.red, 2);
  lineNodes.addChild(xAxis);
 var yAxis = new Line (origin, yEnd, Color.green, 2);
  lineNodes.addChild(yAxis);
 var zAxis = new Line (origin, zEnd, Color.blue, 2);
  lineNodes.addChild(zAxis);
  
 function updateCharacter(character)
 {
 character.setColor(java.awt.Color.orange);
 character.scale(10, 10, 10);
 }
}