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
@RequestMapping("/")
public class CursosController {

    private final CursosRepository cursosRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProfessoresRepository professoresRepository;

    @Autowired
    public CursosController(CursosRepository cursosRepository,
                          CategoriaRepository categoriaRepository,
                          ProfessoresRepository professoresRepository) {
        this.cursosRepository = cursosRepository;
        this.categoriaRepository = categoriaRepository;
        this.professoresRepository = professoresRepository;
    }

    @GetMapping
    public String home(Model model) {
        model.addAttribute("cursos", cursosRepository.findAll());
        model.addAttribute("categorias", categoriaRepository.findAll());
        model.addAttribute("hasSearchTerm", false); // Adicionado
        model.addAttribute("isSearch", false); // Adicionado flag de pesquisa
        return "index";
    }
    
    @GetMapping("/cursos/listar")
    public String listarCursos(Model model) {
        model.addAttribute("cursos", cursosRepository.findAll());
        return "cursos/listar";
    }

    @GetMapping("/categoria/{id}")
public String cursosPorCategoria(@PathVariable Integer id, Model model) {
    List<Cursos> cursos = cursosRepository.findByCategoriaID(id);
    Optional<Categorias> categoria = categoriaRepository.findById(id);
    
    model.addAttribute("cursos", cursos);
    model.addAttribute("categorias", categoriaRepository.findAll());
    model.addAttribute("categoriaSelecionada", categoria.orElse(null));
    model.addAttribute("isSearch", false); // Adicionado flag de pesquisa
    
    return "index";
}

@GetMapping("/pesquisar")
public String pesquisar(@RequestParam String termo, Model model) {
    List<Cursos> cursos = cursosRepository.findByNomeContainingIgnoreCase(termo);
    
    model.addAttribute("cursos", cursos);
    model.addAttribute("categorias", categoriaRepository.findAll());
    model.addAttribute("termoPesquisa", termo);
    model.addAttribute("isSearch", true); // Adicionado flag de pesquisa
    
    return "index";
}

    @GetMapping("/cursos/inserir")
    public String exibirFormulario(Model model) {
        model.addAttribute("cursosDTO", new CursosDTO());
        model.addAttribute("categorias", categoriaRepository.findAll());
        model.addAttribute("professores", professoresRepository.findAll());
        return "cursos/inserir";
    }

    @PostMapping("/cursos/inserir")
    public String inserirCurso(@ModelAttribute @Valid CursosDTO cursosDTO,
                             BindingResult result,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        try {
            if (cursosDTO.getDataInicioStr() != null && !cursosDTO.getDataInicioStr().isEmpty()) {
                cursosDTO.setDataInicio(new SimpleDateFormat("yyyy-MM-dd").parse(cursosDTO.getDataInicioStr()));
            }
            if (cursosDTO.getDataFimStr() != null && !cursosDTO.getDataFimStr().isEmpty()) {
                cursosDTO.setDataFim(new SimpleDateFormat("yyyy-MM-dd").parse(cursosDTO.getDataFimStr()));
            }
        } catch (ParseException e) {
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
        cursosRepository.save(curso);
        redirectAttributes.addFlashAttribute("sucesso", "Curso cadastrado com sucesso!");
        return "redirect:/";
    }

    @GetMapping("/cursos/detalhes/{id}")
    public String detalhesCurso(@PathVariable Integer id, Model model) {
        Optional<Cursos> cursoOpt = cursosRepository.findById(id);
        
        if (cursoOpt.isPresent()) {
            Cursos curso = cursoOpt.get();
            model.addAttribute("curso", curso);
            
            Optional<Categorias> categoria = categoriaRepository.findById(curso.getCategoriaID());
            categoria.ifPresent(cat -> model.addAttribute("categoria", cat));
            
            Optional<Professores> professor = professoresRepository.findById(curso.getProfessorID());
            professor.ifPresent(prof -> model.addAttribute("professor", prof));
            
            return "cursos/detalhes";
        }
        return "redirect:/";
    }

    @GetMapping("/cursos/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        Optional<Cursos> cursoOpt = cursosRepository.findById(id);
        
        if (cursoOpt.isPresent()) {
            model.addAttribute("curso", cursoOpt.get());
            model.addAttribute("categorias", categoriaRepository.findAll());
            model.addAttribute("professores", professoresRepository.findAll());
            return "cursos/editar";
        }
        return "redirect:/";
    }

    @PostMapping("/cursos/editar/{id}")
    public String editado(@ModelAttribute @Valid Cursos curso,
                         BindingResult result,
                         RedirectAttributes msg,
                         @PathVariable Integer id) {
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
            msg.addFlashAttribute("erro", "Formato de data inválido");
            return "redirect:/cursos/editar/" + id;
        }

        if (result.hasErrors()) {
            return "cursos/editar";
        }

        curso.setID(id);
        cursosRepository.save(curso);
        msg.addFlashAttribute("sucesso", "Curso atualizado com sucesso!");
        return "redirect:/";
    }

    @GetMapping("/cursos/excluir/{id}")
    public String excluir(@PathVariable Integer id, RedirectAttributes msg) {
        Optional<Cursos> curso = cursosRepository.findById(id);
        if (curso.isEmpty()) {
            msg.addFlashAttribute("erro", "Curso não encontrado!");
            return "redirect:/";
        }
        cursosRepository.deleteById(id);
        msg.addFlashAttribute("sucesso", "Curso excluído com sucesso!");
        return "redirect:/";
    }
}