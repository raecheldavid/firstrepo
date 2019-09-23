package com.ust.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ust.dao.AdminDao;
import com.ust.model.AdminBean;

@RestController
public class AdminController {

	@Autowired
	AdminDao adao;

	@RequestMapping(value = "/api/getRole", method = RequestMethod.GET)
	public List<AdminBean> getAllRole() {
		List list = adao.getRole();
		return list;
	}

	@RequestMapping(value = "/api/verifyLogin/{uname}/{pass}", method = RequestMethod.GET)
	public AdminBean verifyLogin(@PathVariable("uname") String uname,
			@PathVariable("pass") String pass) {
		return adao.verifyUserLogin(uname, pass);
	}

	@RequestMapping(value = "/api/saveDoctor", method = { RequestMethod.POST,
			RequestMethod.PUT })
	public void doctorInsert(@RequestBody AdminBean aBean) {
		if (aBean.getsId() != 0) {
			adao.updateDoctor(aBean);
		} else {
			adao.insertDoctorStaff(aBean);
			
		}
	}

	@RequestMapping(value = "/api/getDoctorById/{sId}", method = RequestMethod.GET, produces = "application/json")
	public AdminBean getDoctoree(@ModelAttribute("ad") AdminBean ad,
			@PathVariable("sId") int sId) {
		List doc = adao.getDoctorById(sId);
		ad = (AdminBean) doc.get(0);
		return ad;
	}

	/* Request to list the doctor list */
	@RequestMapping(value = "/api/listdoctor", method = RequestMethod.GET)
	public List<AdminBean> getAllDoctors() {
		List list = adao.getDoctorList();
		return list;
	}

	// disable Doctor
	@RequestMapping(value = "/api/disableDoctor", method = RequestMethod.PUT)
	void doctorDisable(@RequestBody AdminBean aBean) {
		adao.disableDoctor(aBean);
	}

	@RequestMapping(value = "/api/disableMedicine", method = RequestMethod.PUT)
	public void disableMedicine(@RequestBody AdminBean aBean) {
		adao.disableMedicine(aBean);
	}

	@RequestMapping(value = "/api/viewMedicineList", method = RequestMethod.GET)
	public List<AdminBean> viewMedicine() {
		List list = adao.viewMedicineList();
		return list;
	}

	@RequestMapping(value = "/api/insertMedicine", method = {
			RequestMethod.POST, RequestMethod.PUT })
	public void insertBook(@RequestBody AdminBean aBean) {

		if (aBean.getMedId() != 0) {
			adao.updateMedicine(aBean);

		} else {
			adao.saveMedicine(aBean);
		}
	}

	@RequestMapping(value = "/api/getMedicineId/{medId}", method = RequestMethod.GET)
	public AdminBean getMedicineById(@PathVariable("medId") int medId) {

		return adao.get(medId);
	}

	/**************** INSERT STAFF ***************************/

	@RequestMapping(value = "/api/insertStaff", method = { RequestMethod.POST,
			RequestMethod.PUT })
	public void staffInsert(@RequestBody AdminBean aBean) {
		if (aBean.getsId() == 0) {
			adao.insertStaff(aBean);
		} else {
			adao.updateStaff(aBean);
		}

	}

	// display staff
	@RequestMapping(value = "/api/staff", method = RequestMethod.GET)
	public List<AdminBean> getAllStaffs() {
		List list = adao.list();
		return list;
	}

	// search by staff name
	@RequestMapping(value = "/api/staff/{sName}", method = RequestMethod.GET)
	public AdminBean getBookById(@PathVariable("sName") String sName) {
		return adao.getStaffName(sName);
	}

	// disable staff
	@RequestMapping(value = "/api/disableStaff", method = RequestMethod.PUT)
	void staffDisable(@RequestBody AdminBean aBean) {
		adao.disableStaff(aBean);

	}
	
	@RequestMapping(value = "/api/staffById/{sId}", method = RequestMethod.GET)
	public AdminBean getBookById(@PathVariable("sId") int sId) {
		AdminBean ab;
		List list= adao.getStaffId(sId);
		ab=(AdminBean) list.get(0);
		return ab;
	}
	
	
	//searches
	
	// search by doctor name
		@RequestMapping(value = "/api/doctor/{searchString}", method = RequestMethod.GET)
		public List<AdminBean> getDoctor(
				@PathVariable("searchString") String searchString) {
			System.out.println(searchString);
			List list = adao.getDoctor(searchString);
			System.out.println(list);
			return list;
		}
		
		
		// search medicine by medicine name or medicine id
		@RequestMapping(value = "/api/medicineSearch/{searchString}", method = RequestMethod.GET)
		public List<AdminBean> getMedicine(
				@PathVariable("searchString") String searchString) {
			System.out.println(searchString);
			List list = adao.getSearchMedicine(searchString);
			System.out.println(list);
			return list;
		}
		
		// search by staff name
		@RequestMapping(value = "/api/staffSearch/{searchString}", method = RequestMethod.GET)
		public List<AdminBean> getPatient(
				@PathVariable("searchString") String searchString) {
			System.out.println(searchString);
			List list = adao.getStaff(searchString);
			System.out.println(list);
			return list;
		}

}
