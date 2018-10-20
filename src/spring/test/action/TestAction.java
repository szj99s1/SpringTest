package spring.test.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import spring.test.kafka.producer.KafkaProducerServer;

@Controller
public class TestAction {

	@Autowired
	private KafkaProducerServer KafkaProducerServer;

	@RequestMapping(value = "/sayHello", method = RequestMethod.GET)
	public Object sayHello(HttpSession session, HttpServletRequest req,
			HttpServletResponse res) throws Exception {
		Map<String, Object> env = new HashMap<String, Object>();
		env.put("sessionId", session.getId());
		return new ModelAndView("recharge", env);
	}

	@RequestMapping(value = "/getName", method = RequestMethod.GET)
	@ResponseBody
	public Map getName(HttpSession session, HttpServletRequest req,
			HttpServletResponse res) throws Exception {
		Map<String, String> env = new HashMap<String, String>();
		env.put("name", "shenzhongjun");
		return env;
	}

	@RequestMapping(value = "/getHello", method = RequestMethod.GET)
	@ResponseBody
	public String getHello(
			@RequestParam(value = "name", required = true) String name) {
		return "Hello World" + name;
	}

	@RequestMapping(value = "/{name}/hello", method = RequestMethod.GET)
	@ResponseBody
	public String setName(HttpServletRequest req, HttpServletResponse resp,
			@PathVariable("name") String name) {

		return "hello " + name;
	}

	@RequestMapping(value = "/getJsp", method = RequestMethod.GET)
	public String getJsp() {

		return "hello";
	}

	@RequestMapping(value = "/sendKafka", method = RequestMethod.GET)
	@ResponseBody
	public String sendKafka(HttpServletRequest req, HttpServletResponse resp) {
		String topic = "order_test_topic";
		String value = "test";
		String ifPartition = "1";
		Integer partitionNum = 2;
		String role = "test";// 用来生成key

		for(int i=0;i<20;i++){
			
		KafkaProducerServer.sndMesForTemplate(topic,
				value+i, ifPartition, partitionNum, role);

		}
		return "success";
	}

}
