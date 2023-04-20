warning: in the working copy of 'app/src/main/res/layout/activity_home.xml', LF will be replaced by CRLF the next time Git touches it
[1mdiff --git a/app/src/main/res/layout/activity_home.xml b/app/src/main/res/layout/activity_home.xml[m
[1mindex 3635503..b857d28 100644[m
[1m--- a/app/src/main/res/layout/activity_home.xml[m
[1m+++ b/app/src/main/res/layout/activity_home.xml[m
[36m@@ -10,6 +10,43 @@[m
     android:paddingTop="13dp"[m
     tools:context=".Signup">[m
 [m
[32m+[m[32m<!--    <androidx.constraintlayout.widget.ConstraintLayout-->[m
[32m+[m[32m<!--        android:layout_width="match_parent"-->[m
[32m+[m[32m<!--        android:layout_height="45dp"-->[m
[32m+[m[32m<!--        android:background="@color/achievement_color">-->[m
[32m+[m
[32m+[m[32m<!--        <LinearLayout-->[m
[32m+[m[32m<!--            android:layout_width="match_parent"-->[m
[32m+[m[32m<!--            android:layout_height="match_parent"-->[m
[32m+[m[32m<!--            android:orientation="vertical"-->[m
[32m+[m[32m<!--            app:layout_constraintBottom_toBottomOf="parent">-->[m
[32m+[m
[32m+[m[32m<!--            <TextView-->[m
[32m+[m[32m<!--                android:id="@+id/textViewTitle"-->[m
[32m+[m[32m<!--                android:layout_width="wrap_content"-->[m
[32m+[m[32m<!--                android:layout_height="wrap_content"-->[m
[32m+[m[32m<!--                android:layout_marginStart="16dp"-->[m
[32m+[m[32m<!--                android:layout_marginTop="12dp"-->[m
[32m+[m[32m<!--                android:fontFamily="@font/inter"-->[m
[32m+[m[32m<!--                android:text="User's you follow achievements between"-->[m
[32m+[m[32m<!--                android:textColor="@color/black"-->[m
[32m+[m[32m<!--                android:textSize="16sp" />-->[m
[32m+[m
[32m+[m[32m<!--            <TextView-->[m
[32m+[m[32m<!--                android:id="@+id/textViewTitleDate"-->[m
[32m+[m[32m<!--                android:layout_width="82dp"-->[m
[32m+[m[32m<!--                android:layout_height="19dp"-->[m
[32m+[m[32m<!--                android:layout_marginTop="13dp"-->[m
[32m+[m[32m<!--                android:layout_marginEnd="16dp"-->[m
[32m+[m[32m<!--                android:layout_marginBottom="16dp"-->[m
[32m+[m[32m<!--                android:fontFamily="@font/inter"-->[m
[32m+[m[32m<!--                android:text="@string/home_header_date"-->[m
[32m+[m[32m<!--                android:textColor="@color/black"-->[m
[32m+[m[32m<!--                android:textSize="14sp" />-->[m
[32m+[m[32m<!--        </LinearLayout>-->[m
[32m+[m
[32m+[m[32m<!--    </androidx.constraintlayout.widget.ConstraintLayout>-->[m
[32m+[m
 [m
     <androidx.recyclerview.widget.RecyclerView[m
         android:id="@+id/recyclerView"[m
