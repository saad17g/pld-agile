package tests;

import models.Intersection;
import models.IntersectionFactory;
import org.junit.Before;
import org.junit.Test;

public class IntersectionTest {
    @Before
    public void setUp(){
        IntersectionFactory.initFactory();
    }
    @Test
    public void constructorTest() {
        Intersection i = IntersectionFactory.createIntersection(25175791L, 45.75406, 4.857418);
        assert(i.getId() == 25175791L);
        assert(i.getLongitude() == 45.75406);
        assert(i.getLatitude() == 4.857418);
    }
}
