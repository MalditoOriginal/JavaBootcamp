package edu.school21.Spring.Preprocessor;

import edu.school21.Spring.Preprocessor.PreProcessor;

public class PreProcessorToUpperImpl implements PreProcessor {
    @Override
    public String process(String message) {
        return message.toUpperCase();
    }
}