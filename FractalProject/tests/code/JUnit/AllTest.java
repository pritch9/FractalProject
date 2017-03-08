package code.JUnit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ BurningShipTest.class, JuliaTest.class, MandelbrotTest.class, MultibrotTest.class })
public class AllTest {

	public void runAll() {

	}

}
