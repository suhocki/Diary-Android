package com.example.liza.superdiary.ui.user;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.bluelinelabs.conductor.RouterTransaction;
import com.bluelinelabs.conductor.changehandler.VerticalChangeHandler;
import com.example.liza.superdiary.R;
import com.example.liza.superdiary.ui.main.MoxyController;
import com.example.liza.superdiary.ui.recycler.RecyclerController;
import com.example.liza.superdiary.ui.start.StartController;

import static com.example.liza.superdiary.ui.recycler.RecyclerController.NOTES;
import static com.example.liza.superdiary.ui.recycler.RecyclerController.NOTIFICATIONS;
import static com.example.liza.superdiary.ui.recycler.RecyclerController.TASKS;

/**
 * Created by User on 15.05.2017.
 */

public class UserController extends MoxyController implements UserView {

    @InjectPresenter
    public UserPresenter userPresenter;

    @Override
    protected View inflateView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.controller_user, container, false);
    }

    @Override
    protected void onViewBound(@NonNull View view) {
        super.onViewBound(view);
        view.findViewById(R.id.buttonNotes).setOnClickListener(view1 -> showRecyclerController(NOTES));
        view.findViewById(R.id.buttonNotifications).setOnClickListener(view1 -> showRecyclerController(NOTIFICATIONS));
        view.findViewById(R.id.buttonTasks).setOnClickListener(view1 -> showRecyclerController(TASKS));
        view.findViewById(R.id.buttonLogout).setOnClickListener(view1 -> userPresenter.logout());
    }

    private void showRecyclerController(int type) {
        getRouter().pushController(RouterTransaction.with(new RecyclerController(type))
                .pushChangeHandler(new VerticalChangeHandler())
                .popChangeHandler(new VerticalChangeHandler()));
    }

    @Override
    public void showStartController() {
        getRouter().setRoot(RouterTransaction.with(new StartController())
                .pushChangeHandler(new VerticalChangeHandler())
                .popChangeHandler(new VerticalChangeHandler()));
    }
}