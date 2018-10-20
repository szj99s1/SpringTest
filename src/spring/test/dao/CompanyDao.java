package spring.test.dao;

import java.util.List;

import spring.test.entity.Company;

public interface CompanyDao {
	public List<Company> getAllCompanys();
	public Company getCompanyById( int id);
    public int add(Company entity);
    public int delete(int id);
    public int update(Company entity);
    public int getNextId();
}
