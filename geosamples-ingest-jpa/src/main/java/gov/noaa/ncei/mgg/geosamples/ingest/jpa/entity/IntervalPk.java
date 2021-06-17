package gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Id;

/*
	FACILITY_CODE VARCHAR2(10) not null
		constraint TEMPQC_INTERVAL_FACILITY_FK
			references CURATORS_FACILITY,
	SHIP_CODE VARCHAR2(4),
	PLATFORM VARCHAR2(50) not null
		constraint TEMPQC_INTERVAL_PLATFORM_FK
			references PLATFORM_MASTER,
	CRUISE VARCHAR2(30) not null,
	SAMPLE VARCHAR2(30) not null,
	DEVICE VARCHAR2(30) not null
		constraint TEMPQC_INTERVAL_DEVICE_FK
			references CURATORS_DEVICE,
	INTERVAL NUMBER(6) not null,
	DEPTH_TOP NUMBER(6),
	DEPTH_TOP_MM NUMBER(2),
	DEPTH_BOT NUMBER(6),
	DEPTH_BOT_MM NUMBER(2),
	DHCORE_ID VARCHAR2(30),
	DHCORE_LENGTH NUMBER(6),
	DHCORE_LENGTH_MM NUMBER(2),
	DHCORE_INTERVAL NUMBER(3),
	DTOP_IN_DHCORE NUMBER(6),
	DTOP_MM_IN_DHCORE NUMBER(2),
	DBOT_IN_DHCORE NUMBER(6),
	DBOT_MM_IN_DHCORE NUMBER(2),
	LITH1 VARCHAR2(40)
		constraint TEMPQC_INTERVAL_LITH1_FK
			references CURATORS_LITHOLOGY,
	TEXT1 VARCHAR2(40)
		constraint TEMPQC_INTERVAL_TEXT1_FK
			references CURATORS_TEXTURE,
	LITH2 VARCHAR2(40)
		constraint TEMPQC_INTERVAL_LITH2_FK
			references CURATORS_LITHOLOGY,
	TEXT2 VARCHAR2(40)
		constraint TEMPQC_INTERVAL_TEXT2_FK
			references CURATORS_TEXTURE,
	COMP1 VARCHAR2(40)
		constraint TEMPQC_INTERVAL_COMP1_FK
			references CURATORS_LITHOLOGY,
	COMP2 VARCHAR2(40)
		constraint TEMPQC_INTERVAL_COMP2_FK
			references CURATORS_LITHOLOGY,
	COMP3 VARCHAR2(40)
		constraint TEMPQC_INTERVAL_COMP3_FK
			references CURATORS_LITHOLOGY,
	COMP4 VARCHAR2(40)
		constraint TEMPQC_INTERVAL_COMP4_FK
			references CURATORS_LITHOLOGY,
	COMP5 VARCHAR2(40)
		constraint TEMPQC_INTERVAL_COMP5_FK
			references CURATORS_LITHOLOGY,
	COMP6 VARCHAR2(40)
		constraint TEMPQC_INTERVAL_COMP6_FK
			references CURATORS_LITHOLOGY,
	DESCRIPTION VARCHAR2(2000),
	AGE VARCHAR2(20)
		constraint TEMPQC_INTERVAL_AGE_FK
			references CURATORS_AGE,
	ABSOLUTE_AGE_TOP VARCHAR2(50),
	ABSOLUTE_AGE_BOT VARCHAR2(50),
	WEIGHT NUMBER(8,3),
	ROCK_LITH VARCHAR2(100)
		constraint TEMPQC_INTERVAL_RKLITH_FK
			references CURATORS_ROCK_LITH,
	ROCK_MIN VARCHAR2(35)
		constraint TEMPQC_INTERVAL_RKMIN_FK
			references CURATORS_ROCK_MIN,
	WEATH_META VARCHAR2(30)
		constraint TEMPQC_INTERVAL_WEATH_FK
			references CURATORS_WEATH_META,
	REMARK VARCHAR2(70)
		constraint TEMPQC_INTERVAL_REMARK_FK
			references CURATORS_REMARK,
	MUNSELL_CODE VARCHAR2(10),
	MUNSELL VARCHAR2(30),
	EXHAUST_CODE VARCHAR2(1),
	PHOTO_LINK VARCHAR2(500),
	LAKE VARCHAR2(50),
	UNIT_NUMBER VARCHAR2(50),
	INT_COMMENTS VARCHAR2(2000),
	DHDEVICE VARCHAR2(50),
	CMCD_TOP NUMBER(6),
	MMCD_TOP NUMBER(1),
	CMCD_BOT NUMBER(6),
	MMCD_BOT NUMBER(1),
	PUBLISH VARCHAR2(1),
	PREVIOUS_STATE VARCHAR2(1),
	IGSN VARCHAR2(9),
	IMLGS VARCHAR2(15),
	PARENT_IGSN VARCHAR2(9),
	constraint TEMPQC_INTERVAL_PK
		primary key (FACILITY_CODE, PLATFORM, CRUISE, SAMPLE, DEVICE, INTERVAL)

 */
public class IntervalPk implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "IMLGS", nullable = false, length = 15)
  private String imlgs;

  @Id
  @Column(name = "INTERVAL", nullable = false)
  private Integer interval;

  public String getImlgs() {
    return imlgs;
  }

  public void setImlgs(String imlgs) {
    this.imlgs = imlgs;
  }

  public Integer getInterval() {
    return interval;
  }

  public void setInterval(Integer interval) {
    this.interval = interval;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IntervalPk that = (IntervalPk) o;
    return Objects.equals(imlgs, that.imlgs) && Objects.equals(interval, that.interval);
  }

  @Override
  public int hashCode() {
    return Objects.hash(imlgs, interval);
  }
}
