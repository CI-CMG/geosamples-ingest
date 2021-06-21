// author Travis Pence, CIRES student employee of MGG, summer 2014
// one post-Travis edit = addition of ending '|' to both output files
class Main{
    static main (String[] args){
        def rows = []
        def cruiseMap = [:]
        def intervalMap = [:]
        def fileName = args[0].split('/')[1].split(/\./)[0] //gets the name of the file from the cmd line before the '.'
        //Deletes the output files if they already exist so that it does not append to the end
        def fileSmp = new File("${System.getProperty("user.dir")}/output/${fileName}.smp")
        if (fileSmp.exists()){fileSmp.delete()}
        def fileInt = new File("/${System.getProperty("user.dir")}/output/${fileName}.int")
        if (fileInt.exists()){fileInt.delete()}
        
        //Open the file from the users current directory
        def file = new File("${System.getProperty("user.dir")}/${args[0]}")
        //Load each row of data into a collection called rows
        file.each{rows << it}
        
        //Run the necessary preliminary checks. 
        def tests = new CheckData(rows)
        tests.runAllTests()
        
        def fatalErrors = tests.getFatalErrors()
        def otherErrors = tests.getErrorReport()
        def warnings = tests.getWarning()
        
        //flags contains the conversion information for the latitude longitude conversion
        def flags = tests.getFlags()
        
        //Prints out all types of errors
        fatalErrors.each{println it}
        otherErrors.each{println it}
        
        //Uncomment the below code to print out warnings such as blank cells.
        //warnings.each{println it}        

        
        //Popup dialog box will show if there are no fatal errors. Files are created automatically if true but will be deleted if the user selects no.
        if (fatalErrors.size() == 0){
            def exec
            def reader = System.in.newReader()
            
            // create new version of readLine that accepts a prompt to remove duplication from the loop
            reader.metaClass.readLine = { String prompt -> println prompt ; readLine() }
            
            // process lines until finished
            while ((exec = reader.readLine("Would you like to create the output files (y/n)?: ")) != 'q') {
                if(exec?.capitalize() == 'Y' ){
                    rows.eachWithIndex{v, i ->
                        def cruise = new UniqueCruise(v.split('\t'), flags[i], fileName)
                        def cruiseID = cruise.getUid()
                        if(!cruiseMap[cruiseID]){
                            cruiseMap.put(cruiseID, cruise)
                        }
                        cruise.writeToINT()
                    }
                    cruiseMap.each{k, v -> v.writeToSMP()}
                    println "Done!"
                    return
                    
                }
                else if(exec?.capitalize() == "N"){
                    println "DELETING...."
                    fileSmp.delete()
                    fileInt.delete()
                    return
                }
                else{
                    println "Please enter 'y' or 'n'"
                }
            
            } 
        }
        else{
            println "Will not generate files while there are fatal errors."
        }          
  }       
}//end main class




class UniqueCruise {
    def smpList=[] //Will contain all fiends in the smpfile in order
    def intList = [] //Will contain all fiends in the intfile in order
    def smpString, intString // These will be the formatted strings that are written to the .int and .smp files.
    def fileName // Used to create the respective .smp and .int files based on the original fileName.
    def uid // This is the unique identification for each cruise. It will be the platform+cruise+sample+device
    
    //The below variables all come from the spreadsheet. They should all be
    //named roughly the same as in the spreadsheet
    def facility_code, ship_code, platform, cruise, sample, device
    def begin_date, end_date, lat, latdeg, latmin, ns, end_lat,end_latdeg
    def end_latmin, end_ns, lon, londeg, lonmin, ew, end_lon, end_londeg
    def end_lonmin, end_ew, latlon_orig, water_depth, end_water_depth
    def storage_meth,cored_length, cored_diam, pi, province, last_update
    def interval, depth_top,depth_top_mm, depth_bot,depth_bot_mm, lith1
    def text1, lith2, text2, comp1, comp2, comp3, comp4, comp5, comp6
    def age, weight, rock_lith, rock_min, weath_meta, remark, munsell
    def exhaust, comment
   
    //Used for the color formatting on the command line.
//    def red = jline.ANSIBuffer.ANSICodes.attrib(31)
//    def reset = jline.ANSIBuffer.ANSICodes.attrib(0)
//    def yellow = jline.ANSIBuffer.ANSICodes.attrib(33)
   
    public UniqueCruise(row, flag, fn){
        //Load data sources and the necessary code to lookup value maps.
        def dataSrc = new DataAccessObject()
        if (!dataSrc.alright()){println "Data source did not load properly, terminating process..." ;System.exit(0)}
        def devMap = dataSrc.getDevMap()
        def shipMap = dataSrc.getShipMap()
        def facMap = dataSrc.getFacMap()
        def lithMap = dataSrc.getLithMap()
        def textMap = dataSrc.getTextMap()
        def provMap = dataSrc.getProvMap()
        def stormethMap = dataSrc.getStormethMap()
        def compMap = dataSrc.getCompMap()
        def munMap = dataSrc.getMunMap()
        def rockMap = dataSrc.getRockMap()
        def ageMap = dataSrc.getAgeMap()
        def drgglassMap = dataSrc.getDrgglassMap()
        def drglithMap = dataSrc.getDrglithMap()
        def drgmetaMap = dataSrc.getDrgmetaMap()
        def drgminMap = dataSrc.getDrgminMap()
        def physprovMap = dataSrc.getPhysprovMap()
        
        /***The below logic removes any whitespace characters from the strings
         * and then capitalizes the first letter of any character string.
         * This works because all the data comes in as an instance of the String object
         ***/
        row = row*.trim()
        row.each{
            if(it){
                it.capitalize()
            }
        }
        
        
        

        fileName = fn
        
        //The elvis operator '?' will return the value if it can find it, else it will set the variable to "Error".
        //"Error" is used as another level of error checking, but it should never be necessary because the code will not run if a fatal error from the previous checking
        //is caught.
        facility_code = facMap[row[0].substring(0,2)] 
        ship_code = row[0]
        platform = shipMap[row[0].substring(0,4)] 
        if(platform == "Error"){
//            println "Fatal Error: The code ${row[0].substring(0,4)} did not return a valid platform name.. terminating script"
            println " Fatal Error: The code ${row[0].substring(0,4)} did not return a valid platform name.. terminating script"
            
        }
        
        cruise = row[1] 
        if(cruise == "Error"){
//            println "Fatal Error:  No value for cruise was entered... terminating script"
            println " Fatal Error: No value for cruise was entered... terminating script"
            System.exit(0)
        }
            
        
        sample = row[2] 
        if(sample == "Error"){
//            println "Fatal Error:  No value for sample was entered... terminating script"
            println " Fatal Error: No value for sample was entered... terminating script"
            System.exit(0)
        }
         
        device = devMap[row[28]] 
        if(device == "Error"){
//            println "Fatal Error:  No value for device code: ${row[28]} was found... terminating script"
            println " Fatal Error: No value for device code: ${row[28]} was found... terminating script"
            System.exit(0)
        }
        
         
        uid = platform+cruise+sample+device // The identifier
        
        begin_date = row[3]
        end_date = row[4]
        
         if(flag == "M"){
             latlon_orig = flag
             (lat, latdeg, latmin, ns, end_lat, end_latdeg, end_latmin, end_ns,
             lon, londeg, lonmin, ew, end_lon, end_londeg, end_lonmin, end_ew) = convertToDecimal(row)}
         else if(flag == "D"){
            latlon_orig = flag
            (lat, latdeg, latmin, ns, end_lat, end_latdeg, end_latmin, end_ns,
            lon, londeg, lonmin, ew, end_lon, end_londeg, end_lonmin, end_ew) = convertToDegrees(row)}
         else{
             println "No conversion flag set, terminating script."
             System.exit(0)
            }
            
        if(row[26].isNumber()){
            water_depth = row[26].toDouble().toInteger()
        }
        else{
            water_depth = row[26]
        }
        
        
        //If the value in row[27] is a string representing a double/float, toInteger() will throw an exception
        //Converting to a double first allows us to then convert it to an integer
        if(row[27].isNumber()){
            end_water_depth = row[27].toDouble().toInteger()
        }
        else{
            end_water_depth = row[27]
        }
        
        storage_meth = stormethMap[row[29]] ?: ""
        cored_length = row[30]
        cored_diam = row[31]
        pi = row[53]
        province = provMap[row[47]] ?: ""
        last_update =  String.format('%tY%<tm%<td', Calendar.getInstance())
        
        //Data used for the .int File
        if(row[32].isNumber()){
            depth_top = row[32].toDouble().toInteger()
        }
        else{
            depth_top = row[32]
        }
        
        if(row[33].isNumber()){
            depth_bot = row[33].toDouble().toInteger()
        }
        else{
            depth_bot = row[33]
        }
        
        interval = row[45].toInteger()
        depth_top_mm = "" //Need to find out what this is!
        depth_bot_mm = ""
        lith1 = lithMap[row[34]] ?: ""
        text1 = textMap[row[35]] ?: ""
        lith2 = lithMap[row[36]] ?: ""
        text2 = textMap[row[37]] ?: lithMap[row[37]] ?: ""
        comp1 = compMap[row[38]] ?: ""
        comp2 = compMap[row[39]] ?: ""
        comp3 = compMap[row[40]] ?: ""
        comp4 = compMap[row[41]] ?: ""
        comp5 = compMap[row[42]] ?: ""
        comp6 = compMap[row[43]] ?: ""
        age = ageMap[row[44]] ?: ""
        weight = row[46]
        rock_lith = drglithMap[row[48]] ?: ""
        rock_min = drgminMap[row[49]] ?: ""
        weath_meta = drgmetaMap[row[50]] ?: ""
        remark = drgglassMap[row[51]] ?: ""
        munsell = row[52]
        exhaust = row[54]
        comment = row[55] //capitalizes the first letter in the comment string if the string is not null.
    } 
    
    //The following method will take the data from the spreadsheet and convert the degree values to decimal
    def convertToDecimal(data){
        lat= latdeg= latmin = ns= end_lat= end_latmin = end_latdeg= end_ns= lon= lonmin= londeg= lonmin= ew= end_lon= end_londeg= end_lonmin= end_ew = ""

        //Starting latitude
        //The if statements ensure that each of the cells have a value input into the cell. Else it won't try
        //to do any conversions.
        
        if(data[5]){
            latmin = data[6] ?: '0'
            def latmint = data[7] ?:'0'
            latdeg= data[5].toInteger()
            
            
            //This line add the minutes and the tenths of minutes together. In the second half
            //it strips any period the tenths of mins may have and places it at the beginning and rounds it to two places
            //Example: 12.34 becomes 0.12
            latmin = latmin.toInteger() + (("."+latmint?.replaceAll(/\./, '')) as BigDecimal).setScale(2,BigDecimal.ROUND_HALF_UP)
            
            lat= ((latdeg + latmin/60) as BigDecimal).setScale(5,BigDecimal.ROUND_HALF_UP)
            if(data[8] != null){
                ns = data[8]
            }
            else{
                if(latdeg < 0){
                    ns = "S"
                }
                else{
                    ns = "N"
                }
            }
            if(ns == "S"){
                lat = -lat
            }
        }//end if
           
    
        //Ending Latitude
        if(data[16]){
            end_latmin = data[17] ?: '0'
            def end_latmint = data[18] ?:'0'
            end_latdeg = data[16].toInteger()
            end_latmin = end_latmin.toInteger() +(("."+end_latmint?.replaceAll(/\./, '')) as BigDecimal).setScale(2,BigDecimal.ROUND_HALF_UP)
            end_lat =((end_latdeg +end_latmin/60) as BigDecimal).setScale(5,BigDecimal.ROUND_HALF_UP)
            if(data[19] != null){
                end_ns = data[19]
            }
            else{
                if(end_latdeg < 0){
                    end_ns = "S"
                }
                else{
                    end_ns = "N"
                }
            }
            
            if(end_ns == "S"){
                end_lat = -end_lat
            }
        }//endif
        
        
        //Starting Longitude
        if(data[9]){
            lonmin = data[10] ?: '0'
            def lonmint = data[11] ?: '0'
            londeg= data[9].toInteger()
            lonmin = lonmin.toInteger() + (("."+lonmint?.replaceAll(/\./, '')) as BigDecimal).setScale(2,BigDecimal.ROUND_HALF_UP)
            lon = ((londeg + lonmin/60) as BigDecimal).setScale(5,BigDecimal.ROUND_HALF_UP)
            if(data[12] !=null){
                ew = data[12]
            }
            else{
                if(londeg < 0){
                    ew = "W"
                }
                else{
                    ew = "E"
                }
            }
            if(ew == "W"){
                lon = -lon
            }
        }//endif
        
        //Ending Longitude
        if(data[20]){
            end_lonmin = data[21] ?:'0'
            def end_lonmint = data[22] ?:'0'
            end_londeg= data[20].toInteger()
            end_lonmin = end_lonmin.toInteger() + (("."+end_lonmint?.replaceAll(/\./, '')) as BigDecimal).setScale(2,BigDecimal.ROUND_HALF_UP)
            end_lon= ((end_londeg + end_lonmin/60) as BigDecimal).setScale(5,BigDecimal.ROUND_HALF_UP)
            if(data[23] !=null){
                end_ew = data[23]
            }
            else{
                if(end_londeg < 0){
                    end_ew = "W"
                }
                else{
                    end_ew = "E"
                }
            }
            if(end_ew == "W"){
                end_lon = -end_lon
            }
        }
        
        //returns all the values that where just set.
        [lat, latdeg, latmin, ns, end_lat, end_latdeg, end_latmin, end_ns, lon, londeg, lonmin, ew, end_lon, end_londeg, end_lonmin, end_ew]
    }//end func
    
    def convertToDegrees(data){
        lat= latdeg= latmin= ns= end_lat= end_latdeg= end_latmin= end_ns= lon= londeg= lonmin= ew= end_lon= end_londeg= end_lonmin= end_ew = ""
        //Starting latitude degrees
        if(data[13].isNumber()){
            lat = (data[13] as BigDecimal).setScale(5,BigDecimal.ROUND_HALF_UP)
            latdeg= lat.toInteger().abs() //Will truncate the value if there is a decimal and return the absoulute value
            latmin = (lat.remainder(BigDecimal.ONE)*60).setScale(2, BigDecimal.ROUND_HALF_UP).abs()
            if(lat < 0){
                ns ="S"
            }
            else{
                ns = "N"
            }
        }
        //Ending Latitude degrees
        if(data[24].isNumber()){
            end_lat= (data[24] as BigDecimal).setScale(5,BigDecimal.ROUND_HALF_UP)
            end_latdeg = end_lat.toInteger().abs()
            end_latmin = (end_lat.remainder(BigDecimal.ONE)*60).setScale(2, BigDecimal.ROUND_HALF_UP).abs()
            if(end_lat < 0){
                end_ns ="S"
            }
            else {
                end_ns = "N"
            }
        }
        //Starting Longitude degrees
        if(data[14].isNumber()){
            lon = data[14].toBigDecimal()setScale(5,BigDecimal.ROUND_HALF_UP)
            londeg = lon.toInteger().abs()
            lonmin = (lon.remainder(BigDecimal.ONE)*60).setScale(2, BigDecimal.ROUND_HALF_UP).abs()
            if(lon < 0){
                ew = "W"
            }
            else{
                ew ="E"
            }
        }
        //Ending Longitude Degrees
        if(data[25].isNumber()){
            end_lon = (data[25] as BigDecimal).setScale(5,BigDecimal.ROUND_HALF_UP)
            end_londeg = end_lon.toInteger().abs()
            end_lonmin = (end_lon.remainder(BigDecimal.ONE)*60).setScale(2, BigDecimal.ROUND_HALF_UP).abs()
            if(end_lon < 0){
                end_ew = "W"
            }
            else{
                end_ew ="E"
            }
        }
        
        //returns the variables that have just been set.
        [lat, latdeg, latmin, ns, end_lat, end_latdeg, end_latmin, end_ns, lon, londeg, lonmin, ew, end_lon, end_londeg, end_lonmin, end_ew]
    }//end func
     
     //Takes a lines collection object and appends them to a file and adds an ending pipe and new line. It then takes the
     //fileName of the object and adds the extension (in this program it will be either .smp or .int
     def writeToFile(line, ext){
        def file = new File("/${System.getProperty("user.dir")}/output//$fileName$ext")
        file.append(line+"|\n")
        }
        
     def writeToSMP(){
        smpList = [facility_code, ship_code, platform, cruise, sample, device, begin_date, end_date, lat, latdeg,latmin, ns,
                  end_lat, end_latdeg, end_latmin, end_ns, lon,londeg, lonmin, ew, end_lon, end_londeg, end_lonmin,
                  end_ew,latlon_orig, water_depth, end_water_depth, storage_meth, cored_length, cored_diam, pi, province, last_update]
        
        smpString = smpList.join('|')
        writeToFile(smpString, ".smp")}
        
     def writeToINT(){
         intList = [facility_code, ship_code, platform, cruise, sample, device, interval, depth_top, depth_top_mm,
               depth_bot, depth_bot_mm,lith1, text1, lith2, text2, comp1, comp2, comp3, comp4, comp5, comp6,
               age, weight, rock_lith, rock_min, weath_meta, remark, munsell, exhaust,comment]
        
        intString = intList.join('|')
        writeToFile(intString, ".int")
     }

}


class CheckData {
    def errorReport = [] //Used to store error messages from each unit test
    def flags = [] // Stores the conversion flags for each row
    def warning = [] //Stores the warning messages for each row
    def fatalErrors = [] //Stores the fatal errors for each row
    def rows
    
    def dataSrc, lithCodes, rockCodes, textCodes, ageCodes, drgglassCodes,drglithCodes,drgmetaCodes
    def drgminCodes, physprovCodes, stormethCodes, shipCodes, facCodes, munCodes, devCodes
   
    //ANSI color codes to print colors on the command line
//    def red = jline.ANSIBuffer.ANSICodes.attrib(31)
//    def reset = jline.ANSIBuffer.ANSICodes.attrib(0)
//    def yellow = jline.ANSIBuffer.ANSICodes.attrib(33)
//    def magenta = jline.ANSIBuffer.ANSICodes.attrib(35)
    
    
    //currentSample stores the identifier(Inst / Ship + CruiseID + SampleID + SampDev) in order to look for
    //multiple lines for the same sample.
    def oldSample, currentSample
    
    //Data stores the current row information from the spreadsheet whereas oldData stores the previous row.
    def data = []
    def oldData = []
    
    def uniqueIDMap = [:]
    def uid
    
    //Constructor.
    public CheckData(r){
        this.dataSrc = new DataAccessObject()
        if(dataSrc.alright()){
            lithCodes = dataSrc.getLithCodes()
            rockCodes = dataSrc.getRockCodes()
            textCodes = dataSrc.getTextCodes()
            ageCodes = dataSrc.getAgeCodes()
            drgglassCodes = dataSrc.getDrgglassCodes()
            drglithCodes = dataSrc.getDrglithCodes()
            drgmetaCodes = dataSrc.getDrgmetaCodes()
            drgminCodes = dataSrc.getDrgminCodes()
            physprovCodes =dataSrc.getPhysprovCodes()
            stormethCodes = dataSrc.getStormethCodes()
            shipCodes = dataSrc.getShipCodes()
            facCodes = dataSrc.getFacCodes()
            munCodes = dataSrc.getMunCodes()
            devCodes = dataSrc.getDevCodes()
            rows = r
        }
        else{println "datasrc failed to load. About to Exit..."; System.exit(1)}
    }//endConstructor
       
    //Compiles all of the tests to be run on the spreadsheet and then prints the report.
    def runAllTests(){
        rows.eachWithIndex{v,i ->
            data = v.split('\t') // Splits the data in each row by the tab escape character '\t'
            data = data*.trim()
            data.each{
                if(it){
                    it.capitalize()
                }
            }
            
            checkBytes(data, i)
            //This make sure that the row of information has been split up correctly into 57 distinct tokens with the last being
            //the '|' character being the last in column 'BE'
            if (data.size() < 57){
//                println "Fatal Error: There should be 57 separate columns for row #${i+1}."
                println " Fatal Error: There should be 57 separate columns for row #${i+1}."
                println "Only ${data.size()} were found. Did you place a '|' after the comment column?"
                println "Program will now terminate..."
                System.exit(0)
            }
            
            
            currentSample =  "${data[0]}${data[1]}${data[2]}${data[28]}"
                     

            
            //The following section will cause fatal errors.
            if(!data[0]){
                fatalErrors << "Fatal Error: No value entered for Inst/Ship in row #${i+1}"
            }
            else if(!shipCodes.contains(data[0])){
                fatalErrors << "Fatal Error: Inst/Ship code: '${data[0]}' is invalid in row #${i+1}"
            }
            
            
            if(!data[1]){
                fatalErrors << "Fatal Error: No value entered for Cruise in row #${i+1}"
            }
            
            
            if(!data[2]){
                fatalErrors << "Fatal Error: No value entered for Sample in row #${i+1}"
            }
            
            if(!data[28]){
                fatalErrors << "Fatal Error: No value entered for Sample Dev Code in row #${i+1}"
            }
            else if(!devCodes.contains(data[28].toString())){
                fatalErrors << "Fatal Error: Device code: '${data[28]}' is invalid in row #${i+1}"
            }
            
            //The following will produce warnings or other errors. These will not prevent the system from running.
            if(!data[44]){
                warning << " Warning: No value entered for Age in row #${i+1} "
            }
            else if(!ageCodes.contains(data[44])){
                errorReport << " Age code: '${data[44]}' is invalid in row #${i+1} "
            }
            
            //Check that the remark lookup code is valid
            if(!data[51]){
                warning << " Warning: No value entered for Drg Glass in row #${i+1}"
            }
            else if(!drgglassCodes.contains(data[51])){
                errorReport << " Drg Glass code: '${data[51]}' is invalid in row #${i+1}"
            }
            
            //Check to see if the Drg Lith lookup code is valid
            if(!data[48]){
                warning << " Warning: No value entered for Drg Lith in row #${i+1}"
            }
            else if(!drglithCodes.contains(data[48])){
                errorReport << " Drg Lith code: '${data[48]}' is invalid in row #${i+1}"
            }
            
            //Check to see if Drg Meta lookup code is valid
            if(!data[50]){
                warning << " Warning: No value entered for Drg Meta in row #${i+1}"
            }
            else if(!drgmetaCodes.contains(data[50])){
                errorReport << " Drg Meta code: '${data[50]}' is invalid in row #${i+1}"
            }
            
            //Check to see if Drg Min lookup code is valid
            if(!data[49]){
                warning << " Warning: No value entered for Drg Min in row #${i+1}"
            }
            else if(!drgminCodes.contains(data[49])){
                errorReport << " Drg Min code: '${data[49]}' is invalid in row #${i+1}"
            }
            
            //Check to see if Phys Prov lookup code is valid
            if(!data[47]){
                warning << " Warning: No value entered for Phys Prov in row #${i+1}"
            }
            else if(!physprovCodes.contains(data[47])){
                errorReport << " Phys Prov code: '${data[47]}' is invalid in row #${i+1}"
            }
            

            
            //Check to see if Facility lookup code (first 2 digits of Inst/Ship value) is valid
            if(!data[0]){
                warning << " Warning: No value entered for Facility in row #${i+1} "
            }
            else if(!facCodes.contains(data[0].substring(0,2))){
                errorReport << " Stor Meth code: '${data[0].substring(0,2)}' is invalid in row #${i+1}"
            }
            
            
            
            //Check to see if Blk Wgt is numeric
            if(data[46] && !data[46].isNumber()){
                errorReport << " Bulk Weight: '${data[46]}' is not numeric in row #${i+1}"
            }
            
            //Check the material exhausted column contains either X, x, or is blank
            if(data[54] && (data[54] != 'X')){
                errorReport << " Material Exhausted: '${data[54]}' needs to be either blank or X in row #${i+1}"
            }
            
            
             //Check to see if Stor Meth lookup code is valid
            if(!data[29]){
                warning << " Warning: No value entered for Stor Meth in row #${i+1}"
            }
            else if(!stormethCodes.contains(data[29])){
                errorReport << " Stor Meth code: '${data[29]}' is invalid in row #${i+1}"
            }
            else if(sameSample()){
                def oldStorMeth = oldData[29]
                if(oldStorMeth != data[29]){
                    errorReport << " Stor Meth Code should be same within the same sample in row #${i} and row #${i+1}"
                }
             }
                
            
            //These checks are a little more complicated and have thus been split out into separate functions. Nothing is returned as they
            //Simply add to the respective error logs.
            checkLithText(data[34], data[35], data[36], data[37], data[38], data[39], data[40], data[41], data[42], data[43], i)
            verifyDate(data[3], i, "start")
            verifyDate(data[4], i, "end")
            checkWaterDepth(data[26], data[27], i)
            checkLatLong(data[5], data[6], data[7], data[8], data[9], data[10], data[11],data[12], data[13], data[14],data[15], i, "starting")
            checkLatLong(data[16], data[17], data[18], data[19], data[20], data[21], data[22],data[23], data[24], data[25],data[15], i, "ending")
            checkCore(data[30], data[31], data[32], data[33], i)
            checkInterval(data[45], i)
            
            
            //Runs a check for duplicate rows
            uid = "$currentSample${data[45]}"
            if(uniqueIDMap.get(uid)){
                errorReport <<" Rows ${uniqueIDMap.get(uid)} and ${i+1} have the same identifier '$uid' (Inst/Ship+CruiseID+SampeID+SampDev+Int#)"
            }
            else{
                uniqueIDMap.put(uid, i+1)
            }
               
            oldSample = currentSample
            oldData = data
        }
}//endfunc

    
    def checkBytes(data, n){
        //println data
        //hard encoding of the byte limits for the spreadsheet for the current format of the spreadsheet.
        def bytesMap = [0:4, 1:30, 2:30, 3:8, 4:8, 5:2, 6:2, 7:2, 8:1, 9:3, 10:2, 11:2, 12:1,
                        13:9, 14:10, 15:1, 16:2, 17:2, 18:2, 19:1, 20:3, 21:2, 22:2, 23:1, 24:9, 25:10,
                        26:5, 27:5, 28:1, 29:1, 30:6, 31:3, 32:6, 33:6, 34:1, 35:1, 36:1, 37:1, 38:1,
                        39:1, 40:1, 41:1, 42:1, 43:1, 44:2, 45:6, 46:8, 47:2, 48:2, 49:1, 50:1, 51:1,
                        52:30, 53:255, 54:1, 55:2000, 56:1]
        def nameMap =  [0:"Inst/Ship Code", 1:"Cruise ID", 2:"Sample ID", 3:"Date collected", 4:"end date", 5:"lat deg", 6:"lat min", 7:"lat tenths of mins",
                        8:"N or S", 9:"lon deg", 10:"lon min", 11:"lon tenths of mins", 12:"E or W", 13:"Dec Lat", 14:"Dec Lon", 15:"Type", 16:"End Lat deg",
                        17:"End lat min", 18:"End tenths of mins", 19:"End N or S", 20:"End Lon Deg", 21:"End Lon min", 22:"End Lon tenths of min", 23:"End E or W",
                        24:"End Dec Lat", 25:"End Dec Lon", 26:"Water Depth", 27:"Ending Water Depth", 28:"Sample Dev code", 29:"Stor Meth", 30:"Core Len",
                        31:"Core Diam", 32:"Depth to Top", 33:"Depth to Bot", 34:"Prim Lith Comp", 35:"Prim text/rock", 36:"Sec Lith comp", 37:"Sec text/rock",
                        38:"OtherComp#1", 39:"OtherComp#2", 40:"OtherComp#3", 41:"OtherComp#4", 42:"OtherComp#5", 43:"OtherComp#6", 44:"Age of Int", 45:"Int #",
                        46:"Blk Wt", 47:"Phys Prov", 48:"Drg Lith", 49:"Drg Min", 50:"Drg Weath & Meta", 51:"Drg Gls Rem",52:"Muns Clr Code",
                        53:"Princ Inv", 54:"Mat Ex", 55:"Comments", 56:"Pipe"]
        
            data.eachWithIndex {
                value, index ->
                        if (value?.getBytes("UTF-8")?.size() > bytesMap[index]){
                            fatalErrors << "Fatal Error: The value '$value' entered in the ${nameMap[index]} has exceeded the byte limit of ${bytesMap[index]} in row #${n+1} "
                        }    
            }
    }//end function
    def checkCore(core_len, core_diam, dep_top, dep_bot, n){
            
            if(dep_top && !dep_top.isNumber()){
                errorReport << " Depth to top: '$dep_top' must be numeric or blank in row #${n+1}"
            }
            else if(dep_bot && !dep_bot.isNumber()){
                errorReport << " Depth to bottom: '$dep_bot' must be numeric or blank in row #${n+1}"
            }
            
            if(core_len && !core_len.isNumber()){
                errorReport << "Fatal Error: Core Length: '$core_len' is not numeric in row #${n+1}"
            }
            else if(core_len && !core_len.isInteger()){
                errorReport << "Fatal Error: Core Length: '$core_len' contains a decimal in row #${n+1}"
            }
            else{
                if(dep_top && dep_bot && !core_len){
                    errorReport << " Core Length  is required if depth to top and depth to bottom are not blank in row #${n+1}"
                }
                else if(dep_top.isInteger() && dep_bot.isInteger()){
                    if(dep_top.toInteger() > core_len.toInteger()){
                        errorReport << " Depth to top: '$dep_top' needs to be <= $core_len in row #${n+1}"
                    }
                    if(dep_bot.toInteger() > core_len.toInteger()){
                        errorReport << " Depth to bottom: '$dep_bot' needs to be <= $core_len in row #${n+1}"
                    }
                }
            }
            
            
            if(core_diam && !core_diam.isNumber()){
                errorReport << " Core Diameter: '$core_diam' is not numeric in row #${n+1}"
            }
            else if(core_diam && !core_diam.isInteger()){
                errorReport << "Fatal Error: Core Diameter: '$core_diam' contains a decimal in row #${n+1}"
            }
            else if(dep_top && !dep_bot ){
                errorReport << "Fatal Error: Depth to bottom  is required if Depth to top is not blank in row #${n+1}"
            }
            else if(!dep_top && dep_bot ){
                errorReport << "Fatal Error: Depth to top is required if Depth to bottom is not blank in row #${n+1}"
            }
            
            
            
            if(sameSample()){
                //Need to cast to Integer to be able to do comparisons correctly.
                def old_core_len =  oldData[30]
                def old_core_diam = oldData[31]
                def old_dep_top = oldData[32]
                def old_dep_bot = oldData[33]
                
                if(!dep_top.isNumber() && old_dep_top.isNumber()){
                    errorReport << "Fatal Error: Depth to top '$dep_top' must be numeric since previous row was '$old_dep_top'."
                }
                else if(dep_top && old_dep_top && (old_dep_top?.toBigDecimal()  > dep_top?.toBigDecimal() )){
                    errorReport <<" Depth to top: should descending from row #${n} to row #${n+1}"
                }
                
                if(!dep_bot.isNumber() && old_dep_bot.isNumber()){
                    errorReport << " Depth to bottom '$dep_bot' must be numeric since previous row was '$old_dep_bot' from row #${n} to row #${n+1}"
                }
                else if(dep_bot && old_dep_bot && (old_dep_bot?.toBigDecimal()  > dep_bot?.toBigDecimal() )){
                    errorReport <<" Depth to bottom: should descending from row #${n} to row #${n+1}"
                }
              
                
                
                if(core_len != old_core_len){
                    errorReport << " Core Lengths differ ($old_core_len, $core_len) in the same sample for row #${n} to row #${n+1}"
                }
                
                if(core_diam != old_core_diam){
                    errorReport << " Core Diameters differ ($old_core_diam, $core_diam) in the same sample for row #${n} to row #${n+1}"
                }               
            }
    }//end function
    
    def checkInterval(interval, n){
        if(!interval.isInteger()){
            fatalErrors << "Fatal Error: The Interval # '$interval' is not a number in row #$n"
        }
        else{
            if(sameSample()){
                def oldInt = oldData[45]
                def targetInt = oldInt.toInteger() + 1
                if(interval.toInteger() != targetInt){
                    fatalErrors << "Fatal Error: The interval # '$interval' needs to increase by one from the previous interval # '$oldInt' in row #${n+1}"
                }
            }
            else if(interval.toInteger() > 1 && !sameSample()){
                fatalErrors << "Fatal Error: The Interval # '$interval' should be 1 in row #${n+1} because it is not the same sample as previous. Current Sample ID is '$currentSample' and the previous is '$oldSample' (Inst/Ship+CruiseID+SampeID+SampDev)"
            }
        }
    }//end function
    //Check to verify that there is data for the lat-long in either decimal or degree
    //format. It then will pass a flag back to the calling function to indicate whether or
    //not the conversion should be run as well as which conversion is needed
    def checkLatLong(lat_deg, lat_min_w, lat_min_t, nors,
        lon_deg, lon_min_w, lon_min_t, eorw, dec_lat, dec_lon, old_flag, n, type){
        def deg_list =[lat_deg, lat_min_w, nors, lon_deg, lon_min_w, eorw]
        def dec_list = [dec_lat, dec_lon]
        def flag = "E" // Used to store the conversion type (N, M, or D) for the current set of data.
        def deg_some = deg_list.any{it.size() > 0}
        def deg_all = deg_list.every{it.size() > 0}
        def dec_some = dec_list.any{it.size() > 0}
        def dec_all = dec_list.every{it.size() >0}
        
        if(deg_some && dec_some){
            if(!old_flag){
                fatalErrors << "Fatal Error: Both  $type degree and decimal fields entered in row with no conversion flag in row #${n+1}"
            }
            else{
                flag = old_flag
                errorReport << " Both $type degree and decimal fields entered:  Will recalculate latitude / longitude using type '$flag' in row ${n+1}"
             }
        }
        else if(!deg_all && deg_some){
            if (lon_deg.size() == 0){
                flag = "N"
                fatalErrors << "Fatal Error: The $type longitude degree field is blank in row #${n+1}"
            }
            else if (lat_deg.size() == 0){
                flag = "N"
                fatalErrors << "Fatal Error: The $type latitude degree field is blank in row #${n+1}"
            }
            else{
                flag = "N"
                fatalErrors << "Lat/Lon degree fields not complete in row  #${n+1} (replace any blank cell with 0)"
            }
        }
        else if(!dec_all && dec_some){
            flag = "N"
            fatalErrors << "Fatal Error: Fields incomplete for decimal $type latitude / longitude fields in row #${n+1}"
        }
        else if(!dec_some && !deg_some && (type == "starting")){
            flag = "N"
            fatalErrors << "Fatal Error: Nothing entered for latitude / longitude fields in row ${n+1}"
        }
        
        else if(deg_some){
            if(lat_deg.isNumber()){
                if(lat_deg.toInteger() < 0 || lat_deg.toInteger() > 90){
                    flag = "N"
                    fatalErrors << "Fatal Error: $type latitude degree '$lat_deg' is out of range (0-90) in row #${n+1}"
                }
            }
            else{
                flag = "N"
                fatalErrors << "Fatal Error: $type latitude degree '$lat_deg' is not numeric in row #${n+1}"
            }
            if(lon_deg.isNumber()){
                if(lon_deg.toInteger() < 0 || lon_deg.toInteger() > 180){
                    flag = "N"
                    fatalErrors << "Fatal Error: $type longitude degree '$lon_deg' is out of range (0-180) in row #${n+1}"
                }
            }
            else{
                flag = "N"
                fatalErrors << "Fatal Error: $type longitude degree '$lon_deg' is not numeric in row #${n+1}"
            }
            
            if(lat_min_w && lat_min_w.isNumber()){
                if(lat_min_w.toInteger() < 0 || lat_min_w.toInteger() > 60){
                    flag = "N"
                    fatalErrors << "Fatal Error: $type latitude minutes '$lat_min_w' out of range (0-60) in row #${n+1}"
                }
            }
            else{
                flag = "N"
                fatalErrors << "Fatal Error: $type latitude minutes '$lat_min_w' is not numeric in row #${n+1}"
                }
            
            
            if(lat_min_t && !lat_min_t.isNumber()){
                flag = "N"
                fatalErrors << "Fatal Error: $type latitude tenths of minutes '$lat_min_t' is not numeric in row #${n+1}"
            }

            
            if(lon_min_w && lon_min_w.isNumber()){
                if (lon_min_w.toInteger() < 0 || lon_min_w.toInteger() > 60){
                    flag = "N"
                    fatalErrors << "Fatal Error: $type longitude minutes '$lon_min_w' out of range (0-60) in row #${n+1}"
                }
            }
            else{
                flag = "N"
                fatalErrors << "Fatal Error: $type longitude whole minutes '$lon_min_w' is not numeric in row #${n+1}"
            }
            if(lon_min_t && !lon_min_t.isNumber()){
                flag = "N"
                fatalErrors << "Fatal Error: $type longitude tenths of minutes '$lon_min_t' is not numeric in row #${n+1}"
            }
            if(nors != "N" && nors != "S"){
                flag = "N"
                fatalErrors << "Fatal Error: $type N or S Field was entered with the invalid code: '$nors' in row #${n+1}"
            }
            if(eorw != "E" && eorw != "W"){
                flag = "N"
                fatalErrors << "Fatal Error: $type E or W Field was entered with the invalid code: '$eorw' in row #${n+1}"
            }
            
        }
        else if(dec_some){
            if(dec_lat.isNumber()){
                if(dec_lat.toDouble() < -90.00 || dec_lat.toDouble() > 90.00){
                    flag = "N"
                    fatalErrors << "Fatal Error: $type decimal latitude '$dec_lat' is out of range (-90 : +90) in row #${n+1}"}
            }
            else{
                flag = "N"
                fatalErrors << "Fatal Error: $type decimal latitude '$dec_lat' is not numeric in row #${n+1}"
            }
            if(dec_lon.isNumber()){if(dec_lon.toDouble() < -180.00 || dec_lon.toDouble() > 180.00){
                flag = "N"
                fatalErrors << "Fatal Error: $type decimal longitude '$dec_lon' is out of range (-180 : +180) in row #${n+1}"}
            }
            else{
                flag = "N"
                fatalErrors << "Fatal Error: $type decimal longitude '$dec_lon' is not numeric in row #${n+1}"
            }
        }
         //Adds the conversion type to the flags list.
        if(type == "starting"){
            if(flag == "E" && deg_some){flags << "M"}
            else if(flag == "E" && dec_some){flags << "D"}
            else {flags << flag}
        }
    }//end func

    def checkLithText(primLith, primText, secLith, secText,c1, c2, c3, c4, c5, c6, n){
        def otherComps = [c1, c2, c3, c4, c5, c6]
        def errors = []
        
        if(primLith && !lithCodes.contains(primLith)){errorReport << " The Primary lithology code: '$primLith' is invalid in row #${n+1}"}
        
        //Both lith.dat and rock.dat cannot contain the texture lookup code.
        if(lithCodes.contains(primLith) && rockCodes.contains(primLith)){
            errorReport << " The Primary texture code: '$primText' cannot be both a lith and rock lookup code in row #${n+1}"
        }
        else if(primText && !textCodes.contains(primText) && !rockCodes.contains(primText)){
            errorReport << " Primary texture: '$primText' is not a valid rock type nor texture in row #${n+1}"
        }
        else if(primText && !textCodes.contains(primText)){
            errorReport << " Primary texture: '$primText' is invalid in row #${n+1}"
        }
    
        //Checks the secondary lith code and text code.
        if(secLith && !lithCodes.contains(secLith)){
            errorReport << " The Secondary lithology code: '$secLith' is invalid in row ${n+1}."
        }
        
        
        if(lithCodes.contains(secLith) && rockCodes.contains(secText)){
            errorReport << " The Secondary texture code: '$secText' cannot be both a lith and rock lookup code in row #${n+1}"
        }
        else if(secText && !textCodes.contains(secText) && !rockCodes.contains(secText)){
            errorReport << " Secondary texture: '$secText' is not a valid rock type nor texture in row #${n+1}"
        }
        else if(secText && !textCodes.contains(secText)){
            errorReport << " Secondary texture: '$secText' is invalid in row #${n+1}"
        }
    
        //If values are entered for the other compositions, check to ensure that they are valid for either the rock.dat file or
        //the lith.dat file. The C code was not clear on whether or not there is a problem if the lookup code exists in both
        //but I am assuming that there is not. If no value has been entered, testing the predicate v will return false
         otherComps.eachWithIndex{
             v, i ->
                 if(v && !lithCodes.contains(v) && !rockCodes.contains(v)){
                     errorReport << " Comp${i}: '$v' is invalid in row #${n+1}"
                  }
         }
    }//endfunction

     //checkWaterDepth ensures that the starting and ending water depth are numeric, integers and are not
     //greater than 11,000
    def checkWaterDepth(start, end, n){
        if(!start){
            warning << " Warning: No value entered for starting depth in row #${n+1}"
        }
        else if(!start.isNumber()){
            errorReport << " The starting depth: '$start' is not numeric in row #${n+1}"
        }
        else if (!start.isInteger()){
            errorReport << " The starting depth: '$start' contains a decimal in row #${n+1}"
        }
        else{
            if (start.toInteger() > 11000){
                errorReport << " The starting depth: '$start' > 11,000 in row #${n+1}"
            }
        }
        
        
        if(!end){
            warning << " Warning: No value entered for ending depth in row #${n+1}"
        }
        else if(!end.isNumber()){
            errorReport << " The ending depth: '$end' is not numeric in row #${n+1}"
        }
        else if (!end.isInteger()){
            errorReport << " The ending depth: '$end' contains a decimal in row #${n+1}"
        }
        else{
            if (end.toInteger() > 11000){
                errorReport << " The ending depth: '$end' > 11,000 in in row #${n+1}"
            }
        }
    }//end function
    
    //returns a boolean true if the previous sample identifier is the same as the current sample identifier
    def sameSample(){
        oldSample == currentSample
    }//end function
        
    //verifyDate will take a date string in the format of yyyymmdd and will ensure that it's a valid date in the range
    //from 19000101 to the present date. It will ensure a leap day is valid and that the day is not out of the range of
    //days in a particular month.
    def verifyDate(String date, n, soe){
        def today = Calendar.getInstance()
        def today_formatted = String.format('%tY%<tm%<td', today) //Format the date retrieved from the system.
        def date_format = new java.text.SimpleDateFormat("yyyyMMdd") //Set the date format for the date checker
        
        if(date.isInteger()){
            if(!date){
                warning <<  " Warning:  No value entered for the $soe date in row #${n+1}"
            }
            else if(date.toString().size() != date_format.toPattern().length()){
                errorReport << " The $soe date: '$date' does not match required length: yyyymmdd in row #${n+1}"
            }
            else if(date.toInteger() < 19000101 || date >today_formatted){
                errorReport << " The $soe date: '$date' out of valid range in row #${n+1}"
            }
            else{
                date_format.setLenient(false)
                try {
                    date_format.parse(date); //verify valid date
                }
                catch (java.text.ParseException pe) {
                    errorReport << " The $soe date: '$date' is an invalid in row #${n+1}" //return the error msg and exit the function
                }
            }
       }
       else{
           //Only the start date column 
           if(!date && soe == "start"){
               errorReport << " The $soe date: '$date' is blank in row #${n+1}"
           }
           else if(date){
               errorReport << " The $soe date: '$date' is non-numeric row #${n+1}"
           }
          
       }
    }//end function
}//end class





class DataAccessObject {
    def devFile = new File("${System.getProperty("user.dir")}/datfiles/cur.dev.dat")
    def shipFile = new File("${System.getProperty("user.dir")}/datfiles/shipname.dat")
    def facFile = new File("${System.getProperty("user.dir")}/datfiles/facility.dat")
    def lithFile =  new File("${System.getProperty("user.dir")}/datfiles/cur.lith.dat")
    def textFile = new File("${System.getProperty("user.dir")}/datfiles/cur.text.dat")
    def provFile = new File("${System.getProperty("user.dir")}/datfiles/cur.prov.dat")
    def stormethFile = new File("${System.getProperty("user.dir")}/datfiles/cur.stor.dat")
    def compFile = new File("${System.getProperty("user.dir")}/datfiles/cur.ocomp.dat")
    def munFile = new File("${System.getProperty("user.dir")}/datfiles/cur.mun.dat")
    def rockFile = new File("${System.getProperty("user.dir")}/datfiles/cur.rock.dat")
    def ageFile = new File("${System.getProperty("user.dir")}/datfiles/cur.age.dat")
    def drgglassFile = new File("${System.getProperty("user.dir")}/datfiles/cur.rem.dat")
    def drglithFile = new File("${System.getProperty("user.dir")}/datfiles/cur.rlith.dat")
    def drgmetaFile = new File("${System.getProperty("user.dir")}/datfiles/cur.weath_meta.dat")
    def drgminFile = new File("${System.getProperty("user.dir")}/datfiles/cur.min.dat")
    def physprovFile = new File("${System.getProperty("user.dir")}/datfiles/cur.prov.dat")
    
    
    
    //Stores the valid lookup codes
    def devCodes = []
    def shipCodes = []
    def facCodes = []
    def lithCodes = []
    def textCodes = []
    def provCodes = []
    def stormethCodes = []
    def compCodes = []
    def munCodes = []
    def rockCodes = []
    def ageCodes = []
    def drgglassCodes = []
    def drglithCodes = []
    def drgmetaCodes = []
    def drgminCodes = []
    def physprovCodes = []
    
    //The maps that will contain the keys and the corressponding lookup values.
    def devMap = [:]
    def shipMap = [:]
    def facMap = [:]
    def lithMap = [:]
    def textMap = [:]
    def provMap = [:]
    def stormethMap = [:]
    def compMap = [:]
    def munMap = [:]
    def rockMap = [:]
    def ageMap = [:]
    def drgglassMap = [:]
    def drglithMap = [:]
    def drgmetaMap = [:]
    def drgminMap = [:]
    def physprovMap = [:]
    
    def errors = []
    def alright = {
        if (errors.size == 0){true}
        else{
            errors.each{println it}
            false
        }
        }
    
    public DataAccessObject(){
    
            if(devFile.exists()){
                devFile.eachLine{
                    def key = it.substring(0,1)
                    def val = it.substring(1).trim()
                    if(!devMap[key]){
                        devMap.put(key,val)
                    }
                    else{println "$key is already in the map for $devFile, system will exit now."; System.exit(0)}
                    }
                    devCodes = devMap.keySet()
            }
            else{println "does not exist"; errors << "$devFile failed to open"}

            if(shipFile.exists()){
                shipFile.eachLine{
                    def key = it.substring(0,4)
                    def val = it.substring(4).trim()
                    if(!shipMap[key]){
                        shipMap.put(key, val)
                    }
                    else{println "$key is already in the map for $shipFile, system will exit now."; System.exit(0)}
                    }
                    shipCodes = shipMap.keySet()
          }
            else{errors << "$shipFile failed to open"}

            if(facFile.exists()){    
                facFile.eachLine{
                    def key = it.substring(0,2)
                    def val = it.substring(2)
                    if(!facMap[key]){
                        facMap.put(key,val)
                    }
                    else{
                        
                        //println "$key is already in the map for $facFile, system will exit now."
                        //System.exit(0)
                    }
               }
                    facCodes = facMap.keySet()
            }
            else{errors << "$facFile failed to open"}

            if(lithFile.exists()){
                lithFile.eachLine{
                    def key = it.substring(0,1)
                    def value = it.substring(1).trim()
                    if (!lithMap[key]){
                        lithMap.put(key,value)
                    }
                    else{println "$key is already in the map for $lithFile, system will exit now."; System.exit(0)}
                }
                lithCodes = lithMap.keySet()
            }
            else{errors << "$lithFile failed to open"}

            if(textFile.exists()){
                textFile.eachLine{
                    def key = it.substring(0,1)
                    def value = it.substring(1).trim()
                    if(!textMap[key]){
                        textMap.put(key, value)
                    }
                    else{println "$key is already in the map for $textFile, system will exit now."}
                }
                textCodes = textMap.keySet()
            }
            else{errors << "$textFile failed to open"}

            if(provFile.exists()){
                provFile.eachLine{
                    def key = it.substring(0,2)
                    def value = it.substring(2).trim()
                    if(!provMap[key]){
                        provMap.put(key, value)
                    }
                    else{println "$key is already in the map for $provFile, system will exit now."; System.exit(0)}
                }
                provCodes = provMap.keySet()
            }
            else{errors << "$provFile failed to open"}
                       
            if(stormethFile.exists()){
                stormethFile.eachLine{
                    def key = it.substring(0,1)
                    def val = it.substring(1).trim()
                    if(!stormethMap[key]){
                        stormethMap.put(key,val)
                    }
                    else{println "$key is already in the map for $stormethFile, system will exit now."; System.exit(0)}
                }
                stormethCodes = stormethMap.keySet()
            }
            else{
                errors << "$stormethFile failed to open"
            }
          
            if(compFile.exists()){
                compFile.eachLine{
                    def key = it.substring(0,1)
                    def val = it.substring(1).trim()
                    if(!compMap[key]){
                        compMap.put(key, val)
                    }
                    else{
                        println "$key already is in the map for $compFile, system will exit now."
                        System.exit(0)
                    }
                }
                compCodes = compMap.keySet()
            }
            else{
                errors << "$compFile failed to open"
            }

            if(munFile.exists()){
                munFile.eachLine{
                    def key = it.substring(0,11).trim()
                    def val = it.substring(11).trim()
                    if(!munMap[key]){
                        munMap.put(key, val)
                    }
                    else{
                        println "$key already is in the map for $munFile, system will exit now."
                        System.exit(0)
                    }
                }
                munCodes = munMap.keySet()
            }
            else{
                errors << "$munFile failed to open"
            }

            if(rockFile.exists()){
                rockFile.eachLine{
                    def key = it.substring(0,1)
                    def val = it.substring(1).trim()
                    if(!rockMap[key]){
                        rockMap.put(key, val)
                    }
                    else{
                        println "$key already is in the map for $rockFile, system will exit now."
                        System.exit(0)
                    }
                }
                rockCodes = rockMap.keySet()
            }
            else{
                errors << "$rockFile failed to open"
            }
         
            if(ageFile.exists()){
                ageFile.eachLine{
                    def key = it.substring(0,2).trim()
                    def val = it.substring(2).trim()
                    if(!ageMap[key]){
                        ageMap.put(key, val)
                    }
                    else{
                        println "$key already is in the map for $ageFile, system will exit now."
                        System.exit(0)
                    }
                }
                ageCodes = ageMap.keySet()
            }
            else{
                errors << "$ageFile failed to open"
            }
                      
            if(drgglassFile.exists()){
                drgglassFile.eachLine{
                    def key = it.substring(0,1).trim()
                    def val = it.substring(1).trim()
                    if(!drgglassMap[key]){
                        drgglassMap.put(key, val)
                    }
                    else{
                        println "$key already is in the map for $drgglassFile, system will exit now."
                        System.exit(0)
                    }
                }
                drgglassCodes = drgglassMap.keySet()
            }
            else{
                errors << "$drgglassFile failed to open"
            }
            
            if(drglithFile.exists()){
                drglithFile.eachLine{
                    def key = it.substring(0,2).trim()
                    def val = it.substring(2).trim()
                    if(!drglithMap[key]){
                        drglithMap.put(key, val)
                    }
                    else{
                        println "$key already is in the map for $drglithFile, system will exit now."
                        System.exit(0)
                    }
                }
                drglithCodes = drglithMap.keySet()
            }
            else{
                errors << "$drglithFile failed to open"
            }
            
            if(drgmetaFile.exists()){
                drgmetaFile.eachLine{
                    def key = it.substring(0,1).trim()
                    def val = it.substring(1).trim()
                    if(!drgmetaMap[key]){
                        drgmetaMap.put(key, val)
                    }
                    else{
                        println "$key already is in the map for $drgmetaFile, system will exit now."
                        System.exit(0)
                    }
                }
                drgmetaCodes = drgmetaMap.keySet()
            }
            else{
                errors << "$drgmetaFile failed to open"
                }
            
            
            if(drgminFile.exists()){
                drgminFile.eachLine{
                    def key = it.substring(0,1).trim()
                    def val = it.substring(1).trim()
                    if(!drgminMap[key]){
                        drgminMap.put(key, val)
                    }
                    else{
                        println "$key already is in the map for $drgminFile, system will exit now."
                        System.exit(0)
                    }
                }
                drgminCodes = drgminMap.keySet()
            }
            else{
                errors << "$drgminFile failed to open"
            }
                        
            if(physprovFile.exists()){
                physprovFile.eachLine{
                    def key = it.substring(0,2).trim()
                    def val = it.substring(2).trim()
                    if(!physprovMap[key]){
                        physprovMap.put(key, val)
                    }
                    else{
                        println "$key already is in the map for $physprovFile, system will exit now."
                        System.exit(0)
                    }
                }
                physprovCodes = physprovMap.keySet()
            }
            else{
                errors << "$physprovFile failed to open"
            }
    }//end constructor
} //end class
