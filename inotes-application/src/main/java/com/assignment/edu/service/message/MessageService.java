package com.assignment.edu.service.message;

import com.assignment.edu.model.Message;
import com.assignment.edu.repository.IMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MessageService implements IMessageService {

    @Autowired
    private IMessageRepository messageRepository;

    @Override
    public Iterable<Message> findAll() {
        return messageRepository.findAll();
    }

    @Override
    public Optional<Message> findById(Long id) {
        return messageRepository.findById(id);
    }

    @Override
    public void save(Message message) {
        messageRepository.save(message);
    }

    @Override
    public void remove(Long id) {
        messageRepository.deleteById(id);
    }
}
