UPDATE CURATORS_CRUISE_LINKS l
    SET DATALINK = REPLACE(l.DATALINK, 'ftp://ftp.whoi.edu/', 'https://scienceweb.whoi.edu/') where LINK_SOURCE = 'WHOI';