package spring.test.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import spring.test.annotation.FieldCheck;
import spring.test.annotation.ParametersCheck;
import spring.test.entity.Company;
import spring.test.exception.ParameterException;
import spring.test.service.CompanyService;
import spring.test.util.RedisCacheConfig;
import spring.test.util.RedisUtil;

@Controller
public class CompanyAction {
	private static Logger logger = Logger.getLogger(CompanyAction.class);
	@Autowired
	@Qualifier("companyService")
	private CompanyService companyService;
	@Autowired
	@Qualifier("redisUtil")
	private RedisUtil redisUtil;
	

	
	@RequestMapping(value = "/company/all", method = RequestMethod.GET)
	@ResponseBody
	public Object allCompanys(HttpServletRequest req, HttpServletResponse resp) throws ParameterException, Exception {
		try {
		List<Company> result = companyService.getAllCompanys();
		return result;
		}catch(Exception e){
			logger.error(e);
			throw new ParameterException(req, "新增失败");
		}
	}
	
	@RequestMapping(value = "/company/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public String delCompany(HttpServletRequest req, HttpServletResponse resp,
			@PathVariable("id") int id) {
		int result = companyService.delete(id);
		return "del " + result;
	}

	@RequestMapping(value = "/company/{name}", method = RequestMethod.PUT)
	@ResponseBody
	public String addCompany(HttpServletRequest req, HttpServletResponse resp,
			@PathVariable("name") String name) throws ParameterException,
			Exception {
		try {
			Company entity = new Company(0, name);
			int result = companyService.add(entity);
			redisUtil.getRedisTemplate().opsForValue().set(String.valueOf(entity.getId()), name);
			redisUtil.getRedisTemplate().opsForHash().put("company", String.valueOf(entity.getId()), name);
			
			logger.info("result:"+result);
			return "add " + result;
		} catch (ParameterException pe) {
			throw pe;
		} catch (Exception e) {
			logger.error(e);
			throw new ParameterException(req, "新增失败");
		}

	}

	@RequestMapping(value = "/company/get", method = RequestMethod.GET)
	@ParametersCheck(checks = { @FieldCheck(name = "id", desc = "主键", length = 20) })
	@ResponseBody
	public Company getCompany(HttpServletRequest req, HttpServletResponse resp,
			@RequestParam(value = "id", required = true) String id)
			throws ParameterException, Exception {
		try {
			logger.info("接收报文:"+id);
			Company company = companyService.getCompanyById(Integer
					.parseInt(id));
			return company;
		} catch (ParameterException pe) {
			throw pe;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new ParameterException(req, "查询失败");
		}
	}
}
