package de.amirrocker.annotationprocessor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import de.amirrocker.annotationprocessor.firstSteps.TestParcel;

import de.amirrocker.binder.Binding;
import de.amirrocker.library.BindView;
import de.amirrocker.library.OnClick;


/**
 * A very simple MainActivity that only showcases the
 * @BindView annotation. See README.md
 *
 * In Short JavaPoet allows:
 * - TypeSpec.Builder => define the class schema
 * - addModifiers(modifier) => add private, public or protected accesss
 * - addAnnotation => add an annotation to the element.
 * - TypeSpec.Builder -> addMethod => adds methods and constructor to class
 * - MethodSpec -> addParameter => add parameter type for a method.
 * - MethodSpec -> addStatement => defines the statements inside the methods. First define placeholders then
 * fill them with values.
 *
 *
 */
public class MainActivity extends AppCompatActivity {

    //this is what we want is implemented.
    @BindView(R.id.tv_testAnnotationProcessing)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TestParcel parcel = new TestParcel("myTestParcel", 1234);
        Log.d("MAIN", "NUMBER created testParcel: "+parcel.toString());

        // bind the view
        Binding.bind(this);

        textView.setText("Some new Text");
    }

    // and this
    @OnClick(R.id.btn_submit)
    public void btn1Click(View v) {
        textView.setText("Button 1 clicked...");
    }

}

