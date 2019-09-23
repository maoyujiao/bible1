package wordtest;

public class BasePresenter<V extends BaseMvpView> {

    V mvpView ;

    public void attach(V view){
        this.mvpView = view;
    }

    public void dettach(){
        mvpView = null ;
    }


}
