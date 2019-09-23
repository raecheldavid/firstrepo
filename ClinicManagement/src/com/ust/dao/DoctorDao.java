package com.ust.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.ust.model.DoctorBean;

public class DoctorDao {

	JdbcTemplate template;

	public void setTemplate(JdbcTemplate template) {
		this.template = template;
	}

	public Integer doc_getLabId(String labName) {
		String sql = "select labId from cm_labTestTable where lName=?";
		return template.queryForObject(sql, new Object[] { labName },
				Integer.class);
	}

	public int addLabTestRequest(DoctorBean doc_bean[]) {
		int arrayObjectLength = doc_bean.length;
		for (int i = 0; i < arrayObjectLength; i++) {

			Integer labId = doc_getLabId(doc_bean[i].getlName());
			String sql = "insert into cm_assignLabTable(regId,dId,labId,assigLabDate,sampleStatus,testStatus)"
					+ "values("
					+ doc_bean[i].getRegId()
					+ ","
					+ doc_bean[i].getdId()
					+ ",?,TO_DATE('"
					+ java.time.LocalDate.now()
					+ "', 'YYYY-MM-DD'),'Yes','Yes')";

			template.update(sql, new Object[] { labId });
		}
		return 1;
	}

	public int getMedicineId(String mName) {
		String sql = "select medId from cm_medicineTable where medName = ?";
		return template.queryForObject(sql, new Object[] { mName },
				Integer.class);

	}

	public int saveDoctorPrescription(DoctorBean d[]) {
		int arrayObjLength = d.length;
		int[] a = new int[arrayObjLength];
		for (int i = 0; i < arrayObjLength; i++) {
			String mName = d[i].getMedName();
			int id = getMedicineId(mName);
			System.out.println("mED iD" + id);
			String sql = "insert into cm_prescriptionTable(dId,regId,medId,medFreq,medDays,prescDate,pharmacyStatus) values("
					+ d[i].getdId()
					+ ","
					+ d[i].getRegId()
					+ ",?,'"
					+ d[i].getMedFreq()
					+ "',"
					+ d[i].getMedDays()
					+ ",TO_DATE('"
					+ java.time.LocalDate.now()
					+ "', 'YYYY-MM-DD'),'" + d[i].getPharmacyStatus() + "')";

			a[i] = template.update(sql, new Object[] { id });
		}
		return 1;

	}

	public int addPatientComments(DoctorBean doc_bean) {

		String sql = "insert into cm_doctorObsTable(obserDate,obsNotes,regId,dId)values(TO_DATE('"
				+ java.time.LocalDate.now()
				+ "', 'YYYY-MM-DD'),'"
				+ doc_bean.getObsNotes()
				+ "',"
				+ doc_bean.getRegId()
				+ ","
				+ doc_bean.getdId() + ")";

		return template.update(sql);
	}

	public List<DoctorBean> lablist() {
		return template.query("select labId ,lName from cm_labTestTable",
				new RowMapper<DoctorBean>() {
					public DoctorBean mapRow(ResultSet rs, int row)
							throws SQLException {
						DoctorBean db = new DoctorBean();
						db.setLabId(rs.getInt(1));
						db.setlName(rs.getString(2));
						return db;
					}
				});
	}

	public List<DoctorBean> medicinelist() {
		return template.query("select medId ,medName from cm_MedicineTable",
				new RowMapper<DoctorBean>() {
					public DoctorBean mapRow(ResultSet rs, int row)
							throws SQLException {
						DoctorBean db = new DoctorBean();
						db.setMedId(rs.getInt(1));
						db.setMedName(rs.getString(2));
						return db;
					}
				});
	}

	
	public int removemedicine(int medId) {
		String sql = "delete from cm_medicineTable where medId = " + medId + "";
		return template.update(sql);

	}

	public List<DoctorBean> lablistHistory(int regId) {
		return template
				.query("select labId,lName from cm_assignLabTable join cm_LabTestTable using(labId) where regId ="
						+ regId + "", new RowMapper<DoctorBean>() {
					public DoctorBean mapRow(ResultSet rs, int row)
							throws SQLException {
						DoctorBean db = new DoctorBean();
						db.setLabId(rs.getInt(1));
						db.setlName(rs.getString(2));
						return db;
					}
				});
	}

	public List<DoctorBean> medicinehist(int regId) {
		return template
				.query("select prescId,medName,prescDate from cm_PrescriptionTable join cm_MedicineTable using(medId) where regId ="
						+ regId + "order by cm_PrescriptionTable.prescDate desc",
						new RowMapper<DoctorBean>() {
							public DoctorBean mapRow(ResultSet rs, int row)
									throws SQLException {
								DoctorBean db = new DoctorBean();
								db.setPrescId(rs.getInt(1));
								db.setMedName(rs.getString(2));
								db.setPrescDate(rs.getDate(3));
								return db;
							}
						});
	}

	public List<DoctorBean> ObslistHistory(int regId) {
		return template
				.query("select cm_doctorObsTable.obsnotes,cm_doctorObsTable.obserdate,cm_StaffTable.sName from cm_doctorObsTable join cm_doctorTable on(cm_doctorObsTable.dId = cm_doctorTable.dId) join cm_StaffTable on(cm_doctorTable.sId = cm_StaffTable.sId)where regId ="
						+ regId + " order by cm_doctorObsTable.obserdate desc",
						new RowMapper<DoctorBean>() {
							public DoctorBean mapRow(ResultSet rs, int row)
									throws SQLException {
								DoctorBean db = new DoctorBean();
								db.setObsNotes(rs.getString(1));
								db.setObserDate(rs.getDate(2));
								db.setsName(rs.getString(3));
								return db;
							}
						});
	}

	// medicine request

	public Integer d_getMedicineId(String medName) {
		String sql = "select medId from cm_medicineTable where medName = ?";
		return template.queryForObject(sql, new Object[] { medName },
				Integer.class);

	}

	public int saveDoctorPrescription(DoctorBean doc) {
		Integer medId = d_getMedicineId(doc.getMedName());
		String sql = "insert into cm_prescriptionTable(dId,regId,medId,medFreq,medDays,prescDate,pharmacyStatus) values("
				+ doc.getdId()
				+ ", "
				+ doc.getRegId()
				+ ",?,'"
				+ doc.getMedFreq()
				+ "',"
				+ doc.getMedDays()
				+ ",TO_DATE('"
				+ java.time.LocalDate.now() + "', 'YYYY-MM-DD'),'no')";
		return template.update(sql, new Object[] { medId });
	}

	public List<DoctorBean> getAllPrescription(int dId,int regId) {
		return template
				.query("select p.dId,p.regId,p.prescdate,p.medDays , p.medFreq ,m.medName from cm_prescriptiontable p join cm_medicineTable m on p.medid=m.medid where trunc(prescdate) = trunc(sysdate) and p.regId = "
						+ regId + " and p.dId= "+dId+"",

				new RowMapper<DoctorBean>() {
					public DoctorBean mapRow(ResultSet rs, int row)
							throws SQLException {
						DoctorBean db = new DoctorBean();
						db.setdId(rs.getInt(1));
						db.setRegId(rs.getInt(2));
						db.setPrescDate(rs.getDate(3));
						db.setMedDays(rs.getInt(4));
						db.setMedFreq(rs.getString(5));
						db.setMedName(rs.getString(6));
						return db;
					}
				});
	}
	
	//Search
	
	// search PatientAppointment by patient name
		public List<DoctorBean> getSearchAppointment(String searchString, int dId) {
			System.out.println(java.time.LocalDate.now());
			return template
					.query("select appId,regId,pFName,pLName,dId,consultStatus,dateOfApp from cm_AppoinmentTable join cm_PatientTable using(regId) where pFName like '"+searchString
							+ "%' and dateOfApp = TO_DATE('"
							+ java.time.LocalDate.now()
							+ "' ,'YYYY-MM-DD') and dId =" + dId + " ",
							new RowMapper<DoctorBean>() {
								public DoctorBean mapRow(ResultSet rs, int row)
										throws SQLException {

									DoctorBean ap = new DoctorBean();
									ap.setAppId(rs.getInt(1));
									ap.setRegId(rs.getInt(2));
									ap.setpFName(rs.getString(3));
									ap.setpLName(rs.getString(4));
									ap.setdId(rs.getInt(5));
									ap.setConsultStatus(rs.getString(6));
									ap.setDateOfApp(rs.getDate(7));

									return ap;
								}
							});

		}
		
		//status
		public List<DoctorBean> getDoctorsCompletedAppointment(int dId) {
			return template
					.query("select appId,regId,pFName,pLName,pGender,consultStatus from cm_PatientTable join cm_AppoinmentTable using(regId) where dId = "
							+ dId
							+ " and dateOfApp = TO_DATE('"
							+ java.time.LocalDate.now() + "' ,'YYYY-MM-DD') and consultStatus = 'yes'",
							new RowMapper<DoctorBean>() {
								public DoctorBean mapRow(ResultSet rs, int row)
										throws SQLException {
									DoctorBean db = new DoctorBean();
									db.setAppId(rs.getInt(1));
									db.setRegId(rs.getInt(2));
									db.setpFName(rs.getString(3));
									db.setpLName(rs.getString(4));
									db.setpGender(rs.getString(5));
									db.setConsultStatus(rs.getString(6));
									return db;
								}
							});
		}
		
		public List<DoctorBean> getDoctorsAnyAppointment(int dId) {
			return template
					.query("select appId,regId,pFName,pLName,pGender,consultStatus from cm_PatientTable join cm_AppoinmentTable using(regId) where dId = "
							+ dId
							+ " and dateOfApp = TO_DATE('"
							+ java.time.LocalDate.now() + "' ,'YYYY-MM-DD')",
							new RowMapper<DoctorBean>() {
								public DoctorBean mapRow(ResultSet rs, int row)
										throws SQLException {
									DoctorBean db = new DoctorBean();
									db.setAppId(rs.getInt(1));
									db.setRegId(rs.getInt(2));
									db.setpFName(rs.getString(3));
									db.setpLName(rs.getString(4));
									db.setpGender(rs.getString(5));
									db.setConsultStatus(rs.getString(6));
									return db;
								}
							});
		}








	public List<DoctorBean> getDoctorsAppointment(int dId) {
			return template
					.query("select appId,regId,pFName,pLName,pGender,consultStatus from cm_PatientTable join cm_AppoinmentTable using(regId) where dId = "
							+ dId
							+ " and dateOfApp = TO_DATE('"
							+ java.time.LocalDate.now() + "' ,'YYYY-MM-DD') and consultStatus='no'",
							new RowMapper<DoctorBean>() {
								public DoctorBean mapRow(ResultSet rs, int row)
										throws SQLException {
									DoctorBean db = new DoctorBean();
									db.setAppId(rs.getInt(1));
									db.setRegId(rs.getInt(2));
									db.setpFName(rs.getString(3));
									db.setpLName(rs.getString(4));
									db.setpGender(rs.getString(5));
									db.setConsultStatus(rs.getString(6));
									return db;
								}
							});
		}

}
