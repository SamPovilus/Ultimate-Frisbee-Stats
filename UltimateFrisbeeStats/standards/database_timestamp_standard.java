//this describes the stanandard way we will put timestamps into the database
java.util.Date today = new java.util.Date();
java.sql.Timestamp ts = new java.sql.Timestamp(today.getTime());
frisbeeData.execSQL("INSERT INTO roster VALUES ( \"" + name + "\"," + number +","+ ts.getTime() + ",0, 0, 0 )");