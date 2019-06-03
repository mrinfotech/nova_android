package com.mrnovacrm.model;

public class SwitchItemDTO{
        private String title;
        private boolean isSwitch;

        public SwitchItemDTO(String title, boolean isSwitch) {
            this.title = title;
            this.isSwitch = isSwitch;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public boolean isSwitch() {
            return isSwitch;
        }

        public void setIsSwitch(boolean isSwitch) {
            this.isSwitch = isSwitch;
        }
}
