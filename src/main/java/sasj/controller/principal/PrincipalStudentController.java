package sasj.controller.principal;

import sasj.data.student.Student;
import sasj.data.student.StudentRepository;
import sasj.config.WebMvcConfig;
import sasj.controller.AuthorizedController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/principal/students")
public class PrincipalStudentController implements AuthorizedController {
    public static final String studentList = "studentList";
    @Autowired
    private StudentRepository studentRepository;

    @GetMapping
    public String list(Model model) {
        Iterable<Student> students = studentRepository.findAll();
        model.addAttribute("students", students);
        return "principal/students";
    }

    @GetMapping(WebMvcConfig.objectIdPathParam)
    public String details(
        @PathVariable(WebMvcConfig.objectIdParamName) Long studentId, Model model
    ) {
        Optional<Student> optionalStudent = studentRepository.findById(studentId);
        if (optionalStudent.isPresent()) {
            model.addAttribute("user", optionalStudent.get().getUser());
            return "principal/user-details"; // TODO
        } else {
            return "";
        }
    }

    @GetMapping(value = "/student-list", name = studentList)
    public String studentList(
        @RequestParam(value = "studentIds", required = false) List<Long> studentIds,
        @RequestParam("id") String id,
        @RequestParam("mode") String mode,
        Model model
    ) {
        Iterable<Student> students;
        if (studentIds != null) {
            students = studentRepository.findByIdInOrderByNumberAsc(studentIds);
        } else {
            students = studentRepository.findAll();
        }
        model.addAttribute("students", students);
        model.addAttribute("id", id);
        model.addAttribute("mode", mode);
        return "principal/fragments/student-list";
    }
}
