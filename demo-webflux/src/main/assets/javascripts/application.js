import 'bootstrap';

import enableCLientSideValidation from "./validation"
window.addEventListener('turbolinks:load', enableCLientSideValidation, false);

import "rails-ujs";
import Turbolinks from "turbolinks";

Turbolinks.start();
