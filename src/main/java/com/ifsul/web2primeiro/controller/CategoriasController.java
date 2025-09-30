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

import com.ifsul.web2primeiro.dtos.CategoriaDTO;
import com.ifsul.web2primeiro.model.Categorias;
import com.ifsul.web2primeiro.model.Cursos;
import com.ifsul.web2primeiro.repository.CategoriaRepository;
import com.ifsul.web2primeiro.repository.CursosRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/categoria")
public class CategoriasController {
    
    @Autowired
    CategoriaRepository repository;
    
    @Autowired
    CursosRepository cursosRepository;

    @GetMapping("/inserir")
    public String inserir() {
        return "categoria/inserir";
    }

    @PostMapping("/inserir")
    public String inserido(
            @ModelAttribute @Valid CategoriaDTO dto,
            BindingResult result, RedirectAttributes msg) {
        if (result.hasErrors()) {
            msg.addFlashAttribute("erro", "Erro ao inserir categoria. Verifique os dados.");
            return "redirect:/categoria/inserir";
        }
        var categoria = new Categorias();
        BeanUtils.copyProperties(dto, categoria);
        repository.save(categoria);
        msg.addFlashAttribute("sucesso", "Categoria cadastrada com sucesso!");
        return "redirect:/categoria/inserir";
    }

    @GetMapping("/listar")
    public ModelAndView listar() {
        ModelAndView mv = new ModelAndView("categoria/listar");
        List<Categorias> lista = repository.findAll();
        mv.addObject("categorias", lista);
        return mv;
    }

    @GetMapping("/excluir/{id}")
    public String confirmarExclusao(@PathVariable(value = "id") int id, Model model) {
        Optional<Categorias> categoria = repository.findById(id);
        if (categoria.isEmpty()) {
            return "redirect:/categoria/listar";
        }
        
        List<Cursos> cursos = cursosRepository.findByCategoriaID(id);
        model.addAttribute("categoria", categoria.get());
        model.addAttribute("cursos", cursos);
        
        return "categoria/confirmar-exclusao";
    }

    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable(value = "id") int id, RedirectAttributes msg) {
        Optional<Categorias> categoria = repository.findById(id);
        if (categoria.isEmpty()) {
            msg.addFlashAttribute("erro", "Categoria não encontrada!");
            return "redirect:/categoria/listar";
        }
        
        List<Cursos> cursos = cursosRepository.findByCategoriaID(id);
        if (!cursos.isEmpty()) {
            msg.addFlashAttribute("erro", 
                "Não é possível excluir a categoria pois existem cursos vinculados a ela.");
            return "redirect:/categoria/listar";
        }
        
        repository.deleteById(id);
        msg.addFlashAttribute("sucesso", "Categoria excluída com sucesso!");
        return "redirect:/categoria/listar";
    }

    @GetMapping("/editar/{id}")
    public ModelAndView editar(@PathVariable(value = "id") int id) {
        ModelAndView mv = new ModelAndView("categoria/editar");
        Optional<Categorias> categoria = repository.findById(id);
        if (categoria.isPresent()) {
            mv.addObject("id", categoria.get().getID());
            mv.addObject("nome", categoria.get().getNome());
        } else {
            mv.setViewName("redirect:/categoria/listar");
        }
        return mv;
    }

    @PostMapping("/editar/{id}")
    public String editado(
            @ModelAttribute @Valid CategoriaDTO dto,
            BindingResult result, RedirectAttributes msg,
            @PathVariable(value = "id") int id) {
        if (result.hasErrors()) {
            msg.addFlashAttribute("erro", "Erro ao editar categoria. Verifique os dados.");
            return "redirect:/categoria/editar/" + id;
        }
        
        Optional<Categorias> cat = repository.findById(id);
        if (cat.isPresent()) {
            var categoria = cat.get();
            BeanUtils.copyProperties(dto, categoria);
            repository.save(categoria);
            msg.addFlashAttribute("sucesso", "Categoria atualizada com sucesso!");
        }
        return "redirect:/categoria/listar";
    }
}