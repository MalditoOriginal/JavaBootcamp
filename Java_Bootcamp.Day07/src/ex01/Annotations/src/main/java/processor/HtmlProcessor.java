package processor;

import annotations.HtmlForm;
import annotations.HtmlInput;
import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

@SupportedAnnotationTypes({
        "annotations.HtmlForm",
        "annotations.HtmlInput"
})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class HtmlProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) {
            return false;
        }

        for (Element element : roundEnv.getElementsAnnotatedWith(HtmlForm.class)) {
            HtmlForm form = element.getAnnotation(HtmlForm.class);
            try {
                FileObject fileObject = processingEnv.getFiler().createResource(
                        StandardLocation.CLASS_OUTPUT, "",
                        form.fileName()
                );

                try (PrintWriter out = new PrintWriter(fileObject.openWriter())) {
                    out.println("<form action = \"" + form.action() + "\" method = \"" + form.method() + "\">");

                    for (Element enclosed : element.getEnclosedElements()) {
                        HtmlInput input = enclosed.getAnnotation(HtmlInput.class);
                        if (input != null) {
                            out.println("\t<input type = \"" + input.type() +
                                    "\" name = \"" + input.name() +
                                    "\" placeholder = \"" + input.placeholder() + "\">");
                        }
                    }

                    out.println("\t<input type = \"submit\" value = \"Send\">");
                    out.println("</form>");
                }

                System.out.println("Generated HTML form: " + fileObject.toUri());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
    }
}
