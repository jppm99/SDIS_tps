package app;

import Control.Controller;

public class App {

    public static void main(String[] args) throws Exception {
        parse_args(args);

        Controller controller = new Controller();
        (new Thread(controller)).start();

        while(true){
            //TODO read input and parse
            
            String[] action = {""};
            controller.add_action(action);
        }
       
    }

    private static void parse_args(String[] args) {
        //TODO
    }
   
}