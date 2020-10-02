package my.edu.tarc.finalyearproject.Domain;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.core.content.ContextCompat;

public class CentralizedFunctions {
    public boolean CheckStringEmpty(String checkString) {
        return checkString.isEmpty();
    }

    public Bitmap CompressImage(Bitmap bitmapImage) {
        //Bitmap bitmapImage = BitmapFactory.decodeFile("Your path");
        int nh = (int) (bitmapImage.getHeight() * (512.0 / bitmapImage.getWidth()));
        Bitmap scaled = Bitmap.createScaledBitmap(bitmapImage, 512, nh, true);
        return scaled;
    }

    public String CheckBookingStatus(String bookingStatus) {
        //Venue Booking Details
        switch (bookingStatus) {
            case "RequestPending":
                return "Pending";
            case "RequestCancelled":
                return "Cancelled";
        }
        return bookingStatus;
    }

    public int getDateDuration(Date startDate, Date endDate) {
        // Positive value
        long difference = Math.abs(endDate.getTime() - startDate.getTime());

        //+ 1 day becuz selected day also counted
        long differenceDates = (difference / (24 * 60 * 60 * 1000)) + 1;

        //Convert long to String
        String dayDifference = Long.toString(differenceDates);

        return Integer.parseInt(dayDifference);
    }

    public boolean CheckAmount(double booking_fee, double max_booking_fee) {
        if (booking_fee <= max_booking_fee) {
            return true;
        } else {
            return false;
        }
    }

    public GradientDrawable getShape(int radius, int color) {
        GradientDrawable shape = new GradientDrawable();
        shape.setCornerRadius(radius);
        shape.setColor(color);

        return shape;
    }

    public int getColor(Context context, int color) {
        return ContextCompat.getColor(context, color);
    }

    public String getDateToString(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        String dateStr = df.format(date);

        return dateStr;
    }

    public float getVenueRating() {
        //set venue rating in venue booking
        Float rating = 0.0f;

        return rating;
    }

    public StringBuilder GetRangeDateString(List<Calendar> selectedDateList) {
        StringBuilder dateList = new StringBuilder();

        int year;
        int month;
        int day;

        if (selectedDateList.size() > 1) {
            for (int r = 0; r <= selectedDateList.size() - 1; r++) {
                if (r == 0 || r == selectedDateList.size() - 1) {
                    year = selectedDateList.get(r).get(Calendar.YEAR);
                    month = selectedDateList.get(r).get(Calendar.MONTH) + 1;
                    day = selectedDateList.get(r).get(Calendar.DAY_OF_MONTH);

                    if (r == selectedDateList.size() - 1) {
                        dateList.append(" - ");
                    }
                    dateList.append(String.format("%02d/%02d/%04d", day, month, year));
                }
            }
        } else if (selectedDateList.size() == 1) {
            Calendar date = selectedDateList.get(0);
            year = date.get(Calendar.YEAR);
            month = date.get(Calendar.MONTH) + 1;
            day = date.get(Calendar.DAY_OF_MONTH);

            dateList.append(String.format("%02d/%02d/%04d - %02d/%02d/%04d", day, month, year, day, month, year));
        }

        return dateList;
    }

    public List<Calendar> getClosureDaytoDateList(List<String> closeDayList) {
        List<Calendar> disableDays = new ArrayList<>();
        int closeDay = 0;
        int weeks = 55; //1 year

        if (!closeDayList.isEmpty()) {
            for (int r = 0; r < closeDayList.size(); r++) {
                switch (closeDayList.get(r)) {
                    case "Monday":
                        closeDay = Calendar.MONDAY;
                        break;
                    case "Tuesday":
                        closeDay = Calendar.TUESDAY;
                        break;
                    case "Wednesday":
                        closeDay = Calendar.WEDNESDAY;
                        break;
                    case "Thursday":
                        closeDay = Calendar.THURSDAY;
                        break;
                    case "Friday":
                        closeDay = Calendar.FRIDAY;
                        break;
                    case "Saturday":
                        closeDay = Calendar.SATURDAY;
                        break;
                    case "Sunday":
                        closeDay = Calendar.SUNDAY;
                        break;
                }

                if (closeDay != 0) {
                    for (int i = 0; i < (weeks * 7); i = i + 7) {
                        Calendar selectedDay = Calendar.getInstance();
                        selectedDay.add(Calendar.DAY_OF_YEAR, (closeDay - selectedDay.get(Calendar.DAY_OF_WEEK) + i));
                        disableDays.add(selectedDay);
                    }
                }
            }
        }
        return disableDays;
    }

    public int getDateDifference(String startDateStr, String endDateStr) {
        // Negative value
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        Date startDate = null;
        Date endDate = null;
        try {
            startDate = sdf.parse(startDateStr);
            endDate = sdf.parse(endDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long difference = endDate.getTime() - startDate.getTime();

        long differenceDates = (difference / (24 * 60 * 60 * 1000));

        //Convert long to String
        String dayDifference = Long.toString(differenceDates);

        return Integer.parseInt(dayDifference);
    }



    //Should put in both booking page of owner & organizer
//    public void ValidateBookingTransactionPayment() {
//        //Check organizer have pay the full payment 1 week in advanced or not;
//        //if not then penalty charged will be given
//        //day 1 : 10 % of deposit
//        //day 2 : add 20 % of deposit
//        //day 3 : add 30 % of deposit
//        //day 4 : add 40 % of deposit
//        //day 5 : Cancel booking & update transaction status to 'Late Payment'
//
//        List<VenueBooking> venueBookingList = new ArrayList<>();
//        List<Transaction> transactionList = new ArrayList<>();
//
//        for (VenueBooking tempVenueBooking : venueBookingList) { //Check Booking Status = Pending
//            if (tempVenueBooking.getBookingStatus().equalsIgnoreCase("Pending")) {
//                for (Transaction tempTransaction : transactionList) { //Find matched transaction ID
//                    if (tempVenueBooking.getTransactionID().equalsIgnoreCase(tempTransaction.getTransactionID())) {
//                        //If transaction status = unpaid; check late payment or not
//                        if (tempTransaction.getTransactionStatus().equalsIgnoreCase("Unpaid")) {
//                            String occupiedStartDate = tempVenueBooking.getOccupiedStartDate();
//                            String currentDate = getTodayDateStr();
//
//                            //currentDate > occupiedStartDate (Negative value = late payment)
//                            int lateDay = getDateDifference(currentDate, occupiedStartDate);
//
//                            if (lateDay < 0) { //Check total payment pay 7 day (1 week) in advanced
//                                lateDay = Math.abs(lateDay);
//                                switch (lateDay) {
//                                    case 1: //total charge 10%
//                                        tempVenueBooking.setChargedDepositPercentage(0.1f);
//                                        break;
//                                    case 2: //total charge 30%
//                                        tempVenueBooking.setChargedDepositPercentage(0.3f);
//                                        break;
//                                    case 3: //total charge 60%
//                                        tempVenueBooking.setChargedDepositPercentage(0.6f);
//                                        break;
//                                    case 4: //total charge 100%
//                                        tempVenueBooking.setChargedDepositPercentage(1.0f);
//                                        break;
//                                    case 5: //Cancel booking & fully charged deposit amt
//                                        tempVenueBooking.setBookingStatus("Cancelled");
//                                        tempVenueBooking.setBookingDepositStatus("Charged");
//                                        break;
//                                }
//
//                                //send notification; notify user
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }

    public String getPenaltyTitle(float percentage) {
        String penaltyTitleStr = "";

        if (percentage == 0.1f) { //late payment (day 1)
            penaltyTitleStr = "Penalty Charged\n(Day 1)";
        } else if (percentage == 0.3f) { //late payment (day 2)
            penaltyTitleStr = "Penalty Charged\n(Day 2)";
        } else if (percentage == 0.6f) { //late payment (day 3)
            penaltyTitleStr = "Penalty Charged\n(Day 3)";
        } else if (percentage == 1.0f) { //late payment (day 4)
            penaltyTitleStr = "Penalty Charged\n(Day 4)";
        }

        return penaltyTitleStr;
    }

    public double getPenaltyCharge(double depositAmt, float percentage) {
        return depositAmt * percentage;
    }

    public String getPenaltyDay(float percentage) {
        String penaltyDayStr = "";

        if (percentage == 0.1f) { //late payment (day 1)
            penaltyDayStr = "(Day 1)";
        } else if (percentage == 0.3f) { //late payment (day 2)
            penaltyDayStr = "(Day 2)";
        } else if (percentage == 0.6f) { //late payment (day 3)
            penaltyDayStr = "(Day 3)";
        } else if (percentage == 1.0f) { //late payment (day 4)
            penaltyDayStr = "(Day 4)";
        }

        return penaltyDayStr;
    }

    public List<String> getCalendartoStr(List<Calendar> calendarList) {
        List<String> calendarStrList = new ArrayList<>();

        for (Calendar tempCalendar : calendarList) {
            int year = tempCalendar.get(Calendar.YEAR);
            int month = tempCalendar.get(Calendar.MONTH) + 1;
            int day = tempCalendar.get(Calendar.DAY_OF_MONTH);

            String dateString = String.format("%02d/%02d/%04d", day, month, year);
            calendarStrList.add(dateString);
        }

        return calendarStrList;
    }

    public List<Calendar> getStrtoCalendarList(List<String> calendarStrList) throws ParseException {
        List<Calendar> calendarList = new ArrayList<>();

        for (String tempStrCalendar : calendarStrList) {
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            Calendar tempCalendar = Calendar.getInstance();
            tempCalendar.setTime(df.parse(tempStrCalendar));

            calendarList.add(tempCalendar);
        }

        return calendarList;
    }



    public String StringtoAMPM(String time, Context mContext) {
        String timeAMPM = "";

        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("hh:mm aa");
        try {
            Date date = dateFormat.parse(time);

            timeAMPM = dateFormat2.format(date);
        } catch (ParseException e) {
            Toast.makeText(mContext, "Convert String to AMPM Error", Toast.LENGTH_SHORT).show();
        }

        return timeAMPM;
    }

    public String CheckSameDate(String startDate, String endDate) {

        String eventDate = startDate + " - " + endDate;

        if (startDate.compareTo(endDate) == 0) {
            eventDate = startDate;
        }

        return eventDate;
    }

    public String getTodayDateStr() {
        Calendar todayDate = Calendar.getInstance();
        int year = todayDate.get(Calendar.YEAR);
        int month = todayDate.get(Calendar.MONTH) + 1;
        int day = todayDate.get(Calendar.DAY_OF_MONTH);

        String todayDateStr = String.format("%02d/%02d/%04d", day, month, year);

        return todayDateStr;
    }

    public boolean CheckOneWeekBooking(Calendar expiryPaymentDate) {
        //expiryPaymentDate = booking occupied start date
        Boolean mustPayWithin3Day;

        expiryPaymentDate.add(Calendar.DATE, -7); // 1 week in advanced
        Date expiredDate = expiryPaymentDate.getTime();
        int dayLeftPayment = getDateDifference(getTodayDateStr(), getDateToString(expiredDate)) + 1;
        //Add back because it refers to the data where it located, not variable itself
        expiryPaymentDate.add(Calendar.DATE, 7);

        if (dayLeftPayment <= 7) {
            //if booked venue occupied start date within 1 weeks;
            //display only full payment and must be paid within 3 days
            mustPayWithin3Day = true;
        } else {
            mustPayWithin3Day = false;
        }

        return mustPayWithin3Day;
    }

    public int CheckPaymentDayLeft(String expiryDepositDateStr) {
        Calendar expiryDepositDate = getStrtoCalendar(expiryDepositDateStr);
        //expiryDepositDate == confirmed date

        expiryDepositDate.add(Calendar.DATE, 3);
        Date expiredDepositDate = expiryDepositDate.getTime();
        expiryDepositDate.add(Calendar.DATE, -3);

        return getDateDifference(getTodayDateStr(), getDateToString(expiredDepositDate));
    }

    public Calendar getStrtoCalendar(String calendarStr) {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Calendar tempCalendar = Calendar.getInstance();
        try {
            tempCalendar.setTime(df.parse(calendarStr));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return tempCalendar;
    }
    public String getTodayDateTimeStr() {
        Calendar todayDate = Calendar.getInstance();
        int year = todayDate.get(Calendar.YEAR);
        int month = todayDate.get(Calendar.MONTH) + 1;
        int day = todayDate.get(Calendar.DAY_OF_MONTH);
        int hour = todayDate.get(Calendar.HOUR_OF_DAY);
        int minute = todayDate.get(Calendar.MINUTE);
        int second = todayDate.get(Calendar.MILLISECOND);

        String todayDateStr = String.format("%02d/%02d/%04d %2d:%2d:%2d", day, month, year, hour, minute, second);

        return todayDateStr;
    }

public void generateTransaction(String uid, Transaction transaction){
    DatabaseReference Root = FirebaseDatabase.getInstance().getReference().getRoot().child("Ewallet").child(uid).child("Activity");
    String tempKey = Root.push().getKey();
    transaction.setTransactionID(tempKey);
    Root.child(tempKey).setValue(transaction);
}



}
