package verf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.netflix.hystrix.exception.HystrixRuntimeException;

import simple.CommandThatFailsFast;
import simple.CommandThatFailsSilently;
import simple.CommandWithStubbedFallback;
import simple.HelloWorldTest;
import simple.CommandWithStubbedFallback.UserAccount;

public class HelloTest {
	@Test
	public void test() throws Exception {
		assertEquals("success", new CommandThatFailsFast(false).execute());
		 try {
		        new CommandThatFailsFast(true).execute();
		        fail("we should have thrown an exception");
		    } catch (HystrixRuntimeException e) {
		        assertEquals("failure from CommandThatFailsFast", e.getCause().getMessage());
		        e.printStackTrace();
		    }
		 assertEquals("success", new CommandThatFailsSilently(false).execute());
		 try {
		        assertEquals(null, new CommandThatFailsSilently(true).execute());
		    } catch (HystrixRuntimeException e) {
		        fail("No exception as we fail silently with a fallback");
		    }
		 CommandWithStubbedFallback command = new CommandWithStubbedFallback(1234, "ca");
	        UserAccount account = command.execute();
	        assertTrue(command.isFailedExecution());
	        assertTrue(command.isResponseFromFallback());
	        assertEquals(1234, account.customerId);
	        assertEquals("ca", account.countryCode);
	        assertEquals(true, account.isFeatureXPermitted);
	        assertEquals(true, account.isFeatureYPermitted);
	        assertEquals(false, account.isFeatureZPermitted);
	}
}
