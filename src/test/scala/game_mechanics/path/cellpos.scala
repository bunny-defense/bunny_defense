import org.scalatest.FlatSpec

import game_mechanics.path.CellPos

class CellPosSpec extends FlatSpec
{
    "A CellPos" should "be created with x and y" in
    {
        val cp = new CellPos(1,2)
        assertResult(1) { cp.x }
        assertResult(2) { cp.y }
    }

    it should "equal a CellPos with identical coordinates" in
    {
        val a = new CellPos(1,2)
        val b = new CellPos(1,2)
        assert(a == b)
    }

    it should "not equal a CellPos with different coordinates" in
    {
        val a = new CellPos(1,2)
        val b = new CellPos(3,4)
        assert( !(a == b) )
    }

    it should "not equal a CellPos with different x coordinates" in
    {
        val a = new CellPos(1,2)
        val b = new CellPos(2,2)
        assert( !(a == b) )
    }

    it should "not equal a CellPos with different y coordinates" in
    {
        val a = new CellPos(1,2)
        val b = new CellPos(1,1)
        assert( !(a == b) )
    }

    it should "not be different than a CellPos with identical coordinates" in
    {
        val a = new CellPos(1,2)
        val b = new CellPos(1,2)
        assert( !(a != b) )
    }

    it should "be different than a CellPos with different coordinates" in
    {
        val a = new CellPos(1,2)
        val b = new CellPos(3,4)
        assert( a != b )
    }

    it should "be different than a CellPos with different x coordinates" in
    {
        val a = new CellPos(1,2)
        val b = new CellPos(2,2)
        assert( a != b )
    }

    it should "be different than a CellPos with different y coordinates" in
    {
        val a = new CellPos(1,2)
        val b = new CellPos(1,1)
        assert( a != b )
    }

    it should "be smaller than a CellPos with bigger x coordinate" in
    {
        val a = new CellPos(1,2)
        val b = new CellPos(2,2)
        assert( a < b )
    }

    it should "be smaller than a CellPos with bigger y coordinate" in
    {
        val a = new CellPos(1,1)
        val b = new CellPos(1,2)
        assert( a < b )
    }

    it should "not be smaller than a cellpos with identical coordinates" in
    {
        val a = new CellPos(1,2)
        val b = new CellPos(1,2)
        assert( !(a < b) )
    }

    it should "not be smaller than a cellpos with smaller x coordinate" in
    {
        val a = new CellPos(2,2)
        val b = new CellPos(1,2)
        assert( !(a < b) )
    }

    it should "not be smaller than a cellpos with smaller y coordinate" in
    {
        val a = new CellPos(1,2)
        val b = new CellPos(1,1)
        assert( !(a < b) )
    }

    it should "add up coordinate by coordinate I" in
    {
        val a = new CellPos(1,2)
        val b = new CellPos(3,4)
        val s = a + b
        assertResult(4) { s.x }
        assertResult(6) { s.y }
    }

    it should "add up coordinate by coordinate II" in
    {
        val a = new CellPos(5,2)
        val b = new CellPos(3,7)
        val s = a + b
        assertResult(8) { s.x }
        assertResult(9) { s.y }
    }

    it should "subtract coordinate by coordinate I" in
    {
        val a = new CellPos(5,4)
        val b = new CellPos(3,2)
        val s = a - b
        assertResult(2) { s.x }
        assertResult(2) { s.y }
    }

    it should "subtract coordinate by coordinate II" in
    {
        val a = new CellPos(9,4)
        val b = new CellPos(3,3)
        val s = a - b
        assertResult(6) { s.x }
        assertResult(1) { s.y }
    }

    it should "increment coordinate by coordinate I" in
    {
        val a = new CellPos(1,2)
        val b = new CellPos(3,4)
        a += b
        assertResult(4) { a.x }
        assertResult(6) { a.y }
    }

    it should "increment coordinate by coordinate II" in
    {
        val a = new CellPos(5,2)
        val b = new CellPos(3,7)
        a += b
        assertResult(8) { a.x }
        assertResult(9) { a.y }
    }

    it should "get multiplied coordinate by coordinate I" in
    {
        val a = new CellPos(1,2)
        val b = a * 2.0
        assertResult(2) { b.x }
        assertResult(4) { b.y }
    }

    it should "get multiplied coordinate by coordinate II" in
    {
        val a = new CellPos(3,2)
        val b = a * 3.0
        assertResult(9) { b.x }
        assertResult(6) { b.y }
    }

    it should "get divided coordinate by coordinate I" in
    {
        val a = new CellPos(1,2)
        val b = a / 2.0
        assertResult(0) { b.x }
        assertResult(1) { b.y }
    }

    it should "get divided coordinate by coordinate II" in
    {
        val a = new CellPos(3,6)
        val b = a / 3.0
        assertResult(1) { b.x }
        assertResult(2) { b.y }
    }

    it should "dot product properly" in
    {
        val a = new CellPos(1,2)
        val b = new CellPos(3,4)
        assertResult(11) { a & b }
    }

    it should "dot product to zero for orthogonal vectors" in
    {
        val a = new CellPos(3,0)
        val b = new CellPos(0,5)
        assertResult(0.0) { a & b }
    }

    it should "compute norm properly" in
    {
        val a = new CellPos(3,4)
        assertResult(5.0) { a.norm() }
    }

    it should "have a norm of 1 if unit" in
    {
        val a = new CellPos(1,0)
        assertResult(1.0) { a.norm() }
    }

    it should "have a norm of 1 if normalized" in
    {
        val a = new CellPos(23,50)
        val b = a.normalize()
        assertResult(1.0) { b.norm() }
    }

    it should "be normalized to itself if unit" in
    {
        val a = new CellPos(1,0)
        val b = a.normalize()
        assertResult(1.0) { b.x }
        assertResult(0.0) { b.y }
    }

    it should "be identical when converted to Waypoint" in
    {
        val a = new CellPos(2,5)
        val b = a.toDouble()
        assertResult(2.0) { b.x }
        assertResult(5.0) { b.y }
    }

    it should "be identical to its clone" in
    {
        val a = new CellPos(2,5)
        val b = a.clone()
        assertResult(a.x) { b.x }
        assertResult(a.y) { b.y }
    }
}
