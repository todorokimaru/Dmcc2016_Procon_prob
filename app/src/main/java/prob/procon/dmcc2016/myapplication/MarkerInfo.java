package prob.procon.dmcc2016.myapplication;

/**
 * Created by maeda on 2016/09/28.
 */

public class MarkerInfo {
    private String date;
    private int info_type;
    private String user_id;
    private String comment;
    private String graph;

    public MarkerInfo(String date, int info_type, String user_id, String comment , String graph){
        this.date = date;
        this.info_type = info_type;
        this.user_id = user_id;
        this.comment = comment;
        this.graph = graph;
    }

    public String returnDate(){
        return date;
    }
    public int returnInfo_type(){
        return info_type;
    }

    public String returnUser_id(){
        return user_id;
    }
    public String returnComment(){
        return comment;
    }
    public String returnGraph(){
        return graph;
    }
}
