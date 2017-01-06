import org.scalatest.FlatSpec

import game_mechanics.path.CellPos
import game_mechanics.path.Waypoint

class WaypointSpec extends FlatSpec
{
    "A Waypoint" should "get initialized with x and y" in
    {
        val a = new Waypoint(1.0, 2.0)
        assertResult(1.0) { a.x }
        assertResult(2.0) { a.y }
    }

    it should "equal a Waypoint with identical coordinates" in
    {
        val a = new Waypoint(1.0, 2.0)
        val b = new Waypoint(1.0, 2.0)
        assert( a == b )
    }

    it should "not equal a Waypoint with different coordinates" in
    {
        val a = new Waypoint(1.0, 2.0)
        val b = new Waypoint(3.0, 5.0)
        assert( !(a == b) )
    }

    it should "not equal a Waypoint with different x coordinates" in
    {
        val a = new Waypoint(1.0, 2.0)
        val b = new Waypoint(3.0, 2.0)
        assert( !(a == b) )
    }

    it should "not equal a Waypoint with different y coordinates" in
    {
        val a = new Waypoint(1.0, 2.0)
        val b = new Waypoint(1.0, 5.0)
        assert( !(a == b) )
    }

    it should "be different than a Waypoint with different coordinates" in
    {
        val a = new Waypoint(1.0, 2.0)
        val b = new Waypoint(3.0, 5.0)
        assert( a != b )
    }

    it should "be different than a Waypoint with different x coordinates" in
    {
        val a = new Waypoint(1.0, 2.0)
        val b = new Waypoint(3.0, 2.0)
        assert( a != b )
    }

    it should "be different than a Waypoint with different y coordinates" in
    {
        val a = new Waypoint(1.0, 2.0)
        val b = new Waypoint(1.0, 5.0)
        assert( a != b )
    }

    it should "not be different than a Waypoint with identical coordinates" in
    {
        val a = new Waypoint(1.0, 2.0)
        val b = new Waypoint(1.0, 2.0)
        assert( !(a != b) )
    }

    it should "add up coordinate by coordinate I" in
    {
        val a = new Waypoint(1.0, 2.0)
        val b = new Waypoint(3.0, 4.0)
        val s = a + b
        assertResult(4.0) { s.x }
        assertResult(6.0) { s.y }
    }

    it should "add up coordinate by coordinate II" in
    {
        val a = new Waypoint(3.0, 2.0)
        val b = new Waypoint(6.0, 5.0)
        val s = a + b
        assertResult(9.0) { s.x }
        assertResult(7.0) { s.y }
    }

    it should "add up coordinate by coordinate with a CellPos I" in
    {
        val a = new Waypoint(1.0, 2.0)
        val b = new CellPos(3,4)
        val s = a + b
        assertResult(4.0) { s.x }
        assertResult(6.0) { s.y }
    }

    it should "add up coordinate by coordinate with a CellPos II" in
    {
        val a = new Waypoint(3.0, 2.0)
        val b = new CellPos(6, 5)
        val s = a + b
        assertResult(9.0) { s.x }
        assertResult(7.0) { s.y }
    }

    it should "subtract coordinate by coordinate I" in
    {
        val a = new Waypoint(5.0, 3.0)
        val b = new Waypoint(3.0, 2.0)
        val s = a - b
        assertResult(2.0) { s.x }
        assertResult(1.0) { s.y }
    }

    it should "subtract coordinate by coordinate II" in
    {
        val a = new Waypoint(9.0, 7.0)
        val b = new Waypoint(6.0, 5.0)
        val s = a - b
        assertResult(3.0) { s.x }
        assertResult(2.0) { s.y }
    }

    it should "subtract with a CellPos coordinate by coordinate I" in
    {
        val a = new Waypoint(5.0, 3.0)
        val b = new CellPos(3, 2)
        val s = a - b
        assertResult(2.0) { s.x }
        assertResult(1.0) { s.y }
    }

    it should "subtract with a CellPos coordinate by coordinate II" in
    {
        val a = new Waypoint(9.0, 7.0)
        val b = new CellPos(6, 5)
        val s = a - b
        assertResult(3.0) { s.x }
        assertResult(2.0) { s.y }
    }

    it should "increment coordinate by coordinate I" in
    {
        val a = new Waypoint(5.0, 3.0)
        val b = new Waypoint(3.0, 2.0)
        a += b
        assertResult(8.0) { a.x }
        assertResult(5.0) { a.y }
    }

    it should "increment coordinate by coordinate II" in
    {
        val a = new Waypoint(1.0, 2.0)
        val b = new Waypoint(3.0, 4.0)
        a += b
        assertResult(4.0) { a.x }
        assertResult(6.0) { a.y }
    }

    it should "increment with a CellPos coordinate by coordinate I" in
    {
        val a = new Waypoint(5.0, 3.0)
        val b = new CellPos(3, 2)
        a += b
        assertResult(8.0) { a.x }
        assertResult(5.0) { a.y }
    }

    it should "increment with a CellPos coordinate by coordinate II" in
    {
        val a = new Waypoint(1.0, 2.0)
        val b = new CellPos(3, 4)
        a += b
        assertResult(4.0) { a.x }
        assertResult(6.0) { a.y }
    }

    it should "get multiplied coordinate by coordinate I" in
    {
        val a = new Waypoint(1.0, 2.0)
        val b = a * 2.0
        assertResult(2.0) { b.x }
        assertResult(4.0) { b.y }
    }

    it should "get multiplied coordinate by coordinate II" in
    {
        val a = new Waypoint(3.0, 5.0)
        val b = a * 3.0
        assertResult( 9.0) { b.x }
        assertResult(15.0) { b.y }
    }

    it should "get divided coordinate by coordinate I" in
    {
        val a = new Waypoint(2.0, 4.0)
        val b = a / 2.0
        assertResult(1.0) { b.x }
        assertResult(2.0) { b.y }
    }

    it should "get divided coordinate by coordinate II" in
    {
        val a = new Waypoint(9.0, 15.0)
        val b = a / 3.0
        assertResult(3.0) { b.x }
        assertResult(5.0) { b.y }
    }

    it should "dot product properly" in
    {
        val a = new Waypoint(1.0, 2.0)
        val b = new Waypoint(3.0, 4.0)
        assertResult(11.0) { a & b }
    }

    it should "dot product to zero if Waypoints are orthogonal" in
    {
        val a = new Waypoint(1.0, 0.0)
        val b = new Waypoint(0.0, 4.0)
        assertResult(0.0) { a & b }
    }

    it should "compute the norm properly" in
    {
        val a = new Waypoint(3.0, 4.0)
        assertResult(5.0) { a.norm() }
    }

    it should "have a norm of 1 if unit" in
    {
        val a = new Waypoint(1.0, 0.0)
        assertResult(1.0) { a.norm() }
    }

    it should "have a norm of 1 if normalized" in
    {
        val a = new Waypoint(23.0, 50.0)
        val b = a.normalize()
        assertResult(1.0) { b.norm() }
    }

    it should "floor the coordinates when converted to CellPos" in
    {
        val a = new Waypoint(2.43, 6.33)
        val b = a.toInt()
        assertResult(2) { b.x }
        assertResult(6) { b.y }
    }

    it should "be identical to its clone" in
    {
        val a = new Waypoint(2.43, 6.33)
        val b = a.clone()
        assert( a == b )
    }
}
