package click.rmx.engine;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import click.rmx.engine.physics.PhysicsWorld;
import click.rmx.engine.physics.PhysicsWorldTest;

@RunWith(Suite.class)
@SuiteClasses({
	PhysicsWorldTest.class
})
public class RMXEngineTests {

}
