// eagerly import theme styles so as we can override them
import '@vaadin/vaadin-lumo-styles/all-imports';

import '@vaadin/vaadin-charts/theme/vaadin-chart-default-theme';

const $_documentContainer = document.createElement('template');

$_documentContainer.innerHTML = `
<custom-style>
  <style>
    html {
      --lumo-font-size: 1rem;
      --lumo-font-size-xxxl: 1.75rem;
      --lumo-font-size-xxl: 1.375rem;
      --lumo-font-size-xl: 1.125rem;
      --lumo-font-size-l: 1rem;
      --lumo-font-size-m: 0.875rem;
      --lumo-font-size-s: 0.8125rem;
      --lumo-font-size-xs: 0.75rem;
      --lumo-font-size-xxs: 0.6875rem;
      --lumo-line-height-m: 1.4;
      --lumo-line-height-s: 1.2;
      --lumo-line-height-xs: 1.1;
      --lumo-size-xl: 3rem;
      --lumo-size-l: 2.5rem;
      --lumo-size-m: 2rem;
      --lumo-size-s: 1.75rem;
      --lumo-size-xs: 1.5rem;
      --lumo-space-xl: 1.875rem;
      --lumo-space-l: 1.25rem;
      --lumo-space-m: 0.625rem;
      --lumo-space-s: 0.3125rem;
      --lumo-space-xs: 0.1875rem;
    }

    [theme~="dark"] {
    }
  </style>
</custom-style>

<dom-module id="button-style" theme-for="vaadin-button">
  <template>
    <style>
      :host(:not([theme~="tertiary"])) {
        background-image: linear-gradient(var(--lumo-tint-5pct), var(--lumo-shade-5pct));
        box-shadow: inset 0 0 0 1px var(--lumo-contrast-20pct);
      }

      :host(:not([theme~="tertiary"]):not([theme~="primary"]):not([theme~="error"]):not([theme~="success"])) {
        color: var(--lumo-body-text-color);
      }

      :host([theme~="primary"]) {
        text-shadow: 0 -1px 0 var(--lumo-shade-20pct);
      }
    </style>
  </template>
</dom-module>

<dom-module id="text-field-style" theme-for="vaadin-text-field vaadin-text-area vaadin-password-field">
  <template>
    <style>
      [part="input-field"] {
        box-shadow: inset 0 0 0 1px var(--lumo-contrast-30pct);
        background-color: var(--lumo-base-color);
      }

      :host([invalid]) [part="input-field"] {
        box-shadow: inset 0 0 0 1px var(--lumo-error-color);
      }
    </style>
  </template>
</dom-module>

<custom-style>
  <style>
    html {
      overflow:hidden;
    }
  </style>
</custom-style>

<dom-module id="app-layout" theme-for="vaadin-app-layout">
  <template>
    <style>
      :host(:not([dir='rtl']):not([overlay])) [part='drawer'] {
        border-right: none;
        box-shadow: var(--lumo-box-shadow-s);
        background-color: var(--lumo-base-color);
        z-index: 1;
      }
      :host([dir='rtl']:not([overlay])) [part='drawer'] {
        border-left: none;
        box-shadow: var(--lumo-box-shadow-s);
        background-color: var(--lumo-base-color);
        z-index: 1;
      }
      [part='navbar'] {
        box-shadow: var(--lumo-box-shadow-s);
      }
    </style>
  </template>
</dom-module>

<dom-module id="chart" theme-for="vaadin-chart">
  <template>
    <style include="vaadin-chart-default-theme">
      :host {
        --vaadin-charts-color-0: var(--lumo-primary-color);
        --vaadin-charts-color-1: var(--lumo-error-color);
        --vaadin-charts-color-2: var(--lumo-success-color);
        --vaadin-charts-color-3: var(--lumo-contrast);
      }
      .highcharts-container {
        font-family: var(--lumo-font-family);
      }
      .highcharts-background {
        fill: var(--lumo-base-color);
      }
      .highcharts-title {
        fill: var(--lumo-header-text-color);
        font-size: var(--lumo-font-size-xl);
        font-weight: 600;
        line-height: var(--lumo-line-height-xs);
      }
      .highcharts-legend-item text {
        fill: var(--lumo-body-text-color);
      }
      .highcharts-axis-title,
      .highcharts-axis-labels {
        fill: var(--lumo-secondary-text-color);
      }
      .highcharts-axis-line,
      .highcharts-grid-line,
      .highcharts-tick {
        stroke: var(--lumo-contrast-10pct);
      }
      .highcharts-column-series rect.highcharts-point {
        stroke: var(--lumo-base-color);
      }
    </style>
  </template>
</dom-module>`;

document.head.appendChild($_documentContainer.content);
