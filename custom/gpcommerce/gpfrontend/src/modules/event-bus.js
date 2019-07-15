// This module includes event buses that will be used across the application
import Vue from 'vue';

export const globalEventBus = new Vue();
export const eventBus = new Vue();
export const cartEventBus = new Vue();
export const checkoutEventBus = new Vue();
