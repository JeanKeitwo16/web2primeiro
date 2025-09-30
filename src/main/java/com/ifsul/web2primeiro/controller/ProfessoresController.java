package com.ifsul.web2primeiro.controller;

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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ifsul.web2primeiro.dtos.ProfessorDTO;
import com.ifsul.web2primeiro.model.Professores;
import com.ifsul.web2primeiro.model.Cursos;
import com.ifsul.web2primeiro.repository.ProfessoresRepository;
import com.ifsul.web2primeiro.repository.CursosRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/professores")
public class ProfessoresController {
    @Autowired
    ProfessoresRepository repository;
    
    @Autowired
    CursosRepository cursosRepository;

    @GetMapping("/inserir")
    public String inserir() {
        return "professores/inserir";
    }

    @PostMapping("/inserir")
    public String inserido(
            @ModelAttribute @Valid ProfessorDTO dto,
            BindingResult result, RedirectAttributes msg) {
        if (result.hasErrors()) {
            msg.addFlashAttribute("erro", "Erro ao cadastrar professor. Verifique os dados.");
            return "professores/inserir";
        }
        var professores = new Professores();
        BeanUtils.copyProperties(dto, professores);
        repository.save(professores);
        msg.addFlashAttribute("sucesso", "Professor cadastrado com sucesso!");
        return "redirect:/professores/listar";
    }

    @GetMapping("/listar")
    public ModelAndView listar() {
        ModelAndView mv = new ModelAndView("professores/listar");
        List<Professores> lista = repository.findAll();
        mv.addObject("professores", lista);
        return mv;
    }

    @GetMapping("/excluir/{id}")
    public String confirmarExclusao(@PathVariable(value = "id") int id, Model model) {
        Optional<Professores> professor = repository.findById(id);
        if (professor.isEmpty()) {
            return "redirect:/professores/listar";
        }
        
        List<Cursos> cursos = cursosRepository.findByProfessorID(id);
        model.addAttribute("professor", professor.get());
        model.addAttribute("cursos", cursos);
        
        return "professores/confirmar-exclusao";
    }

    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable(value = "id") int id, RedirectAttributes msg) {
        Optional<Professores> professor = repository.findById(id);
        if (professor.isEmpty()) {
            msg.addFlashAttribute("erro", "Professor não encontrado!");
            return "redirect:/professores/listar";
        }
        
        List<Cursos> cursos = cursosRepository.findByProfessorID(id);
        if (!cursos.isEmpty()) {
            msg.addFlashAttribute("erro", 
                "Não é possível excluir o professor pois existem cursos vinculados a ele.");
            return "redirect:/professores/listar";
        }
        
        repository.deleteById(id);
        msg.addFlashAttribute("sucesso", "Professor excluído com sucesso!");
        return "redirect:/professores/listar";
    }

    @GetMapping("/editar/{id}")
    public ModelAndView editar(@PathVariable(value = "id") int id) {
        ModelAndView mv = new ModelAndView("professores/editar");
        Optional<Professores> professores = repository.findById(id);
        if (professores.isPresent()) {
            mv.addObject("professor", professores.get());
        } else {
            mv.setViewName("redirect:/professores/listar");
        }
        return mv;
    }

    @PostMapping("/editar/{id}")
    public String editado(
            @ModelAttribute @Valid ProfessorDTO dto,
            BindingResult result, RedirectAttributes msg,
            @PathVariable(value = "id") int id) {
        if (result.hasErrors()) {
            msg.addFlashAttribute("erro", "Erro ao editar professor. Verifique os dados.");
            return "redirect:/professores/editar/" + id;
        }
        Optional<Professores> prof = repository.findById(id);
        if (prof.isPresent()) {
            var professores = prof.get();
            BeanUtils.copyProperties(dto, professores);
            repository.save(professores);
            msg.addFlashAttribute("sucesso", "Professor atualizado com sucesso!");
        }
        return "redirect:/professores/listar";
    }
}