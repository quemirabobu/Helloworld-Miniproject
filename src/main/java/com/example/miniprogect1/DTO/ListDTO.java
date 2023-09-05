package com.example.miniprogect1.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListDTO {

        private int page;
        private int recordSize;

        public ListDTO(){
            this.page = 1;
            this.recordSize = 5;
        }
        public int GetOffset(){
            return (page -1)*recordSize;
        }
}
