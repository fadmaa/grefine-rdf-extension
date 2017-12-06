/*
 * Function invoked to initialize the extension.
 */
function init() {
  var RefineServlet = Packages.com.google.refine.RefineServlet;
  
  //RefineServlet.cacheClass(Packages.org.deri.orefine.rdf.SaveRdfSchemaOperation$RdfSchemaChacnge);
	
  /*
   *  Attach an rdf schema to each project.
   */
  Packages.com.google.refine.model.Project.registerOverlayModel("rdfSchema", Packages.org.deri.orefine.rdf.RdfSchema);
  
  /*
   *  Operations
   */
  Packages.com.google.refine.operations.OperationRegistry.registerOperation(
        module, "save-rdf-schema", Packages.org.deri.orefine.rdf.SaveRdfSchemaOperation);
  
  /*
   *  Exporters
   */
  var ExporterRegistry = Packages.com.google.refine.exporters.ExporterRegistry;
  var RdfExporter = Packages.org.deri.grefine.rdf.exporters.RdfExporter;
    
  ExporterRegistry.registerExporter("rdf", new Packages.org.deri.orefine.rdf.RdfExporter("rdf-xml"));
  ExporterRegistry.registerExporter("Turtle", new Packages.org.deri.orefine.rdf.RdfExporter("ttl"));
    
  /*
   *  GREL Functions and Binders
  */
  Packages.com.google.refine.grel.ControlFunctionRegistry.registerFunction(
      "urlify", new Packages.org.deri.orefine.rdf.expr.Urlify());
   
    
  /*
  *  Commands
  */
  RefineServlet.registerCommand(module, "initialise-schema", new Packages.org.deri.orefine.rdf.commands.InitialiseSchemaCommand());
  RefineServlet.registerCommand(module, "save-rdf-schema", new Packages.org.deri.orefine.rdf.commands.SaveRdfSchemaCommand());
  RefineServlet.registerCommand(module, "save-baseURI", new Packages.org.deri.orefine.rdf.commands.SaveBaseURICommand());
  RefineServlet.registerCommand(module, "suggest-term", new Packages.org.deri.orefine.rdf.commands.SuggestTermCommand());
  RefineServlet.registerCommand(module, "preview-rdf", new Packages.org.deri.orefine.rdf.commands.PreviewRdfCommand());
  RefineServlet.registerCommand(module, "preview-rdf-expression", new Packages.org.deri.orefine.rdf.commands.PreviewRdfValueExpressionCommand());
  

  var ClientSideResourceManager = Packages.com.google.refine.ClientSideResourceManager;
    
  // Script files to inject into /project page
  ClientSideResourceManager.addPaths(
    "project/scripts",
    module,
    [
      "scripts/rdf-schema/vocab/prefix-manager.js",
      "scripts/rdf-schema/node-canvas.js",
      "scripts/rdf-schema/link-canvas.js",
      "scripts/rdf-schema/canvas.js",
      "scripts/rdf-schema/vocab/new-prefix-widget.js",
      "scripts/rdf-schema/vocab/suggestterm.suggest.js",
      "scripts/rdf-schema/commands.js",
      "scripts/menu-bar-extensions.js",
      "scripts/rdf-schema/rdf-expressions-grel-view.js"
    ]
  );
  
  // Style files to inject into /project page
  ClientSideResourceManager.addPaths(
    "project/styles",
    module,
    [
      "styles/schema-alignment-dialog.css",
      "styles/rdf-schema.css"
    ]
  );
}

function process(path, request, response) {
  if (path == "/" || path == "") {
    butterfly.redirect(request, response, "/");
  }
}
