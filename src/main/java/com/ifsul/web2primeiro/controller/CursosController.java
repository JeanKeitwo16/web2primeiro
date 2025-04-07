package com.ifsul.web2primeiro.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ifsul.web2primeiro.dtos.CursosDTO;
import com.ifsul.web2primeiro.model.Categorias;
import com.ifsul.web2primeiro.model.Cursos;
import com.ifsul.web2primeiro.model.Professores;
import com.ifsul.web2primeiro.repository.CategoriaRepository;
import com.ifsul.web2primeiro.repository.CursosRepository;
import com.ifsul.web2primeiro.repository.ProfessoresRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/cursos")
public class CursosController {

    @Autowired
    CursosRepository repository;
    CategoriaRepository categoriaRepository;
    ProfessoresRepository professoresRepository;

    @GetMapping("/inserir")
    public String exibirFormulario(Model model) {
        // Use apenas o construtor vazio
        model.addAttribute("cursosDTO", new CursosDTO());
        model.addAttribute("categorias", categoriaRepository.findAll());
        model.addAttribute("professores", professoresRepository.findAll());
        return "cursos/inserir";
    }

    @Autowired
    public CursosController(CursosRepository repository,
                          CategoriaRepository categoriaRepository,
                          ProfessoresRepository professoresRepository) {
        this.repository = repository;
        this.categoriaRepository = categoriaRepository;
        this.professoresRepository = professoresRepository;
    }

    @PostMapping("/inserir")
public String inserirCurso(
        @ModelAttribute @Valid CursosDTO cursosDTO,
        BindingResult result,
        RedirectAttributes redirectAttributes,
        Model model) {

    // Conversão manual das datas
    try {
        if (cursosDTO.getDataInicioStr() != null && !cursosDTO.getDataInicioStr().isEmpty()) {
            cursosDTO.setDataInicio(new SimpleDateFormat("yyyy-MM-dd").parse(cursosDTO.getDataInicioStr()));
        }
        if (cursosDTO.getDataFimStr() != null && !cursosDTO.getDataFimStr().isEmpty()) {
            cursosDTO.setDataFim(new SimpleDateFormat("yyyy-MM-dd").parse(cursosDTO.getDataFimStr()));
        }
    } catch (ParseException e) {
        e.printStackTrace();
        redirectAttributes.addFlashAttribute("erro", "Formato de data inválido");
        return "redirect:/cursos/inserir";
    }

    if (result.hasErrors()) {
        model.addAttribute("categorias", categoriaRepository.findAll());
        model.addAttribute("professores", professoresRepository.findAll());
        return "cursos/inserir";
    }

    Cursos curso = new Cursos();
    BeanUtils.copyProperties(cursosDTO, curso);

    repository.save(curso);
    redirectAttributes.addFlashAttribute("sucesso", "Curso cadastrado com sucesso!");
    return "redirect:/cursos/listar";
}

    @GetMapping("/detalhes/{id}")
    public ModelAndView detalhesCurso(@PathVariable Integer id) {
        ModelAndView mv = new ModelAndView("cursos/detalhes");
        Optional<Cursos> cursoOpt = repository.findById(id);
        
        if (cursoOpt.isPresent()) {
            Cursos curso = cursoOpt.get();
            mv.addObject("curso", curso);
            
            // Carrega a categoria relacionada
            Optional<Categorias> categoria = categoriaRepository.findById(curso.getCategoriaID());
            categoria.ifPresent(cat -> mv.addObject("categoria", cat));
            
            // Carrega o professor relacionado
            Optional<Professores> professor = professoresRepository.findById(curso.getProfessorID());
            professor.ifPresent(prof -> mv.addObject("professor", prof));
            
        } else {
            mv.setViewName("redirect:/cursos/listar");
        }
        
        return mv;
    }

    @GetMapping("/listar")
    public ModelAndView listar() {
        ModelAndView mv = new ModelAndView("cursos/listar");
        List<Cursos> lista = repository.findAll();
        mv.addObject("cursos", lista);
        return mv;
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable(value = "id") int id, RedirectAttributes msg) {
        Optional<Cursos> curso = repository.findById(id);
        if (curso.isEmpty()) {
            msg.addFlashAttribute("erro", "Curso não encontrado!");
            return "redirect:/cursos/listar";
        }
        repository.deleteById(id);
        msg.addFlashAttribute("sucesso", "Curso excluído com sucesso!");
        return "redirect:/cursos/listar";
    }

    @GetMapping("/editar/{id}")
public ModelAndView editar(@PathVariable(value = "id") int id) {
    ModelAndView mv = new ModelAndView("cursos/editar");
    Optional<Cursos> cursoOpt = repository.findById(id);
    
    if (cursoOpt.isPresent()) {
        Cursos curso = cursoOpt.get();
        mv.addObject("curso", curso);
        mv.addObject("categorias", categoriaRepository.findAll());
        mv.addObject("professores", professoresRepository.findAll());
    } else {
        mv.setViewName("redirect:/cursos/listar");
    }
    return mv;
}
    @GetMapping("/pesquisar")
public ModelAndView pesquisar(@RequestParam String termo) {
    ModelAndView mv = new ModelAndView("index"); // Assume que sua tela inicial é "index.html"
    
    // Implemente a pesquisa conforme sua necessidade
    List<Cursos> cursos = repository.findByNomeContainingIgnoreCase(termo);
    
    mv.addObject("cursos", cursos);
    mv.addObject("categorias", categoriaRepository.findAll()); // Mantém as categorias no menu
    
    return mv;
}

    @PostMapping("/editar/{id}")
public String editado(
        @ModelAttribute @Valid Cursos curso,
        BindingResult result, 
        RedirectAttributes msg,
        @PathVariable(value = "id") int id) {
    
    // Conversão manual das datas se necessário
    try {
        String dataInicioStr = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
            .getRequest().getParameter("dataInicioStr");
        String dataFimStr = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
            .getRequest().getParameter("dataFimStr");
        
        if (dataInicioStr != null && !dataInicioStr.isEmpty()) {
            curso.setDataInicio(new SimpleDateFormat("yyyy-MM-dd").parse(dataInicioStr));
        }
        if (dataFimStr != null && !dataFimStr.isEmpty()) {
            curso.setDataFim(new SimpleDateFormat("yyyy-MM-dd").parse(dataFimStr));
        }
    } catch (ParseException e) {
        e.printStackTrace();
        msg.addFlashAttribute("erro", "Formato de data inválido");
        return "redirect:/cursos/editar/" + id;
    }

    if (result.hasErrors()) {
        msg.addFlashAttribute("erro", "Erro ao editar curso. Verifique os dados.");
        return "redirect:/cursos/editar/" + id;
    }

    curso.setID(id); // Garante que o ID correto será mantido
    repository.save(curso);
    msg.addFlashAttribute("sucesso", "Curso atualizado com sucesso!");
    return "redirect:/cursos/listar";
}
}