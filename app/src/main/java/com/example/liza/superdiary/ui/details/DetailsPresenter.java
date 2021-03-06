package com.example.liza.superdiary.ui.details;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.liza.superdiary.App;
import com.example.liza.superdiary.database.DatabaseRepo;
import com.example.liza.superdiary.database.models.Note;
import com.example.liza.superdiary.database.models.Notification;
import com.example.liza.superdiary.database.models.Task;
import com.example.liza.superdiary.preferences.PreferencesRepo;

import javax.inject.Inject;

/**
 * Created by User on 17.05.2017.
 */

@InjectViewState
public class DetailsPresenter extends MvpPresenter<DetailsView> {

    @Inject
    DatabaseRepo databaseRepo;

    @Inject
    PreferencesRepo preferencesRepo;

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        App.appComponent.inject(this);
    }

    void saveNotification(String text, String date, String time) {
        try {
            if (isAllCorrect(text, date, time))
                preferencesRepo
                        .getCurrentLogin()
                        .flatMap(login -> databaseRepo.getUser(login))
                        .flatMapCompletable(user -> databaseRepo.addNotification(user, new Notification(text, time + "\n" + date)))
                        .doOnComplete(() -> getViewState().showListController())
                        .subscribe();
            else getViewState().showToast("Некорректные данные");
        } catch (Exception e) {
            getViewState().showToast("Некорректные данные");
        }
    }


    void saveNote(String text) {
        if (text.length() > 255)
            getViewState().showToast("Превышено максимальное количество символов.");
        else preferencesRepo
                .getCurrentLogin()
                .flatMap(login -> databaseRepo.getUser(login))
                .flatMapCompletable(user -> databaseRepo.addNote(user, new Note(text)))
                .doOnComplete(() -> getViewState().showListController())
                .subscribe();
    }

    void saveTask(String text) {
        if (text.length() > 255)
            getViewState().showToast("Превышено максимальное количество символов.");
        else preferencesRepo
                .getCurrentLogin()
                .flatMap(login -> databaseRepo.getUser(login))
                .flatMapCompletable(user -> databaseRepo.addTask(user, new Task(text)))
                .doOnComplete(() -> getViewState().showListController())
                .subscribe();
    }

    void updateNote(Note note, String text) {
        if (text.length() > 255)
            getViewState().showToast("Превышено максимальное количество символов.");
        else {
            note.setText(text);
            preferencesRepo
                    .getCurrentLogin()
                    .flatMap(login -> databaseRepo.getUser(login))
                    .flatMapCompletable(user -> databaseRepo.updateNote(user, note))
                    .doOnComplete(() -> getViewState().showListController())
                    .subscribe();
        }
    }

    void updateNotification(Notification notification, String text, String date, String time) {
        try {
            if (isAllCorrect(text, date, time)) {
                notification.setText(text);
                notification.setTime(time + "\n" + date);
                preferencesRepo
                        .getCurrentLogin()
                        .flatMap(login -> databaseRepo.getUser(login))
                        .flatMapCompletable(user -> databaseRepo.updateNotification(user, notification))
                        .doOnComplete(() -> getViewState().showListController())
                        .subscribe();
            } else getViewState().showToast("Некорректные данные");

        } catch (Exception e) {
            getViewState().showToast("Некорректные данные");
        }
    }

    void updateTask(Task task, String text) {
        if (text.length() > 255)
            getViewState().showToast("Превышено максимальное количество символов.");
        else {
            task.setText(text);
            preferencesRepo
                    .getCurrentLogin()
                    .flatMap(login -> databaseRepo.getUser(login))
                    .flatMapCompletable(user -> databaseRepo.updateTask(user, task))
                    .doOnComplete(() -> getViewState().showListController())
                    .subscribe();
        }
    }

    private static boolean isAllCorrect(String text, String date, String time) {
        return (isTextNotEmpty(text)
                && isTextCorrect(text)
                && isDateCorrect(date)
                && isTimeCorrect(time));
    }

    public static boolean isTimeCorrect(String time) {
        return (time.length() == 5
                && (time.split(":").length == 2)
                && (Integer.valueOf(time.split(":")[0]) < 24)
                && (Integer.valueOf(time.split(":")[0]) >= 0)
                && (Integer.valueOf(time.split(":")[1]) >= 0)
                && (Integer.valueOf(time.split(":")[1]) < 60));
    }

    public static boolean isDateCorrect(String date) {
        return (date.split(" ").length == 2)
                && (Integer.valueOf(date.split(" ")[0]) <= 31)
                && (date.split(" ")[1].length() >= 3);
    }

    public static boolean isTextCorrect(String text) {
        return text.length() <= 255;
    }

    public static boolean isTextNotEmpty(String text) {
        return !text.isEmpty();
    }
}
