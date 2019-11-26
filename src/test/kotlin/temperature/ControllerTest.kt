//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.http.HttpStatus
//import kotlin.test.assertEquals
//import kotlin.test.assertNotNull
//import org.junit.runner.RunWith
//
//import org.junit.Assert.assertTrue
//import org.junit.Test
//import org.junit.runner.RunWith
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.SpringApplicationConfiguration
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
//import org.springframework.test.context.web.WebAppConfiguration
//
//
//@RunWith(SpringRunner::class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//class ControllerTest {
//    @Autowired
//    lateinit var testRestTemplate: TestRestTemplate
//    @Test
//    fun testHelloController() {
//        val result = testRestTemplate.getForEntity("/hello/string", String::class.java)
//        assertNotNull(result)
//        assertEquals(result.statusCode, HttpStatus.OK)
//        assertEquals(result.body, "Hello string!")
//    }
//    @Test
//    fun testHelloService() {
//        val result = testRestTemplate.getForEntity("/hello/service", String::class.java)
//        assertNotNull(result)
//        assertEquals(result.statusCode, HttpStatus.OK)
//        assertEquals(result.body, "Hello service!")
//    }
//    @Test
//    fun testHelloDto() {
//        val result = testRestTemplate.getForEntity("/hello/data", Hello::class.java)
//        assertNotNull(result)
//        assertEquals(result.statusCode, HttpStatus.OK)
//        assertEquals(result.body, Hello("Hello data!"))
//    }
//}
//
