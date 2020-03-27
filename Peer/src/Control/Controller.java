package Control;

import Actions.*;
import Communication.MC_listener;

import java.util.ArrayList;

//TODO
public class Controller implements Runnable {

    private ArrayList<String[]> actions;

    public Controller() {
        actions = new ArrayList<String[]>();

        MC_listener mc_listener = new MC_listener();
        (new Thread(mc_listener)).start();
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub

        while(true) {
            if (actions.size() > 0) {
                //TODO
                
                Action action = parse_action(actions.remove(actions.size()-1));
                (new Thread(action)).start();

            } else {
                try {
                    Thread.sleep(100); // arg in milis (10^-3s)
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private Action parse_action(String[] action){
        Action obj;

        //TODO
        switch (action[0]) {
            case "BACKUP":
                obj = new Backup();
                break;
            
            case "DELETE":
                obj = new Delete_file();
                break;
            
            case "RECLAIM":
                obj = new Reclaim_space();
                break;
            
            case "RESTORE":
                obj = new Restore();
                break;
            default:
                obj = null;
                break;
        }

        return obj;
    }

    public void add_action(String[] action){
        this.actions.add(action);
    }
    
}