import * as JSPDF from 'jspdf';

const PDF = {
  data() {
    return {
      // Default export is a4 paper, portrait, using milimeters for units
      pdf: new JSPDF('p', 'pt', 'a4'),
    };
  },
  methods: {
    renderPDF(props) {
      const sourceContent =
        document.querySelector(`#${props.source}`) ||
        document.querySelector(`.${props.source}`);
      this.pdf.fromHTML(
        sourceContent, // Content source
        props.position.x, // x co-ord
        props.position.y, // y co-ord
        {
          width: props.width, // max width of content on PDF
          elementHandler: props.callback(this.pdf), // Callback for handling specific scenario
        },
      );
    },
    downLoadPDF() {
      this.pdf.save(this.name);
    },
    openPDF() {
      this.pdf.output('dataurlnewwindow');
    },
  },
};

export default PDF;
