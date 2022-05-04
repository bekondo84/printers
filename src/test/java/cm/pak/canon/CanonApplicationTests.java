package cm.pak.canon;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.Assert;

import javax.annotation.Resource;

@SpringBootTest
class CanonApplicationTests {

	@Resource
	BCryptPasswordEncoder encoder ;

	@Test
	void contextLoads() {
	    final String pwd = "nimda";
		System.out.println(encoder.encode(pwd));
		Assert.isNull(null, "ys");
	}

}
