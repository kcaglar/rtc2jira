package to.rtc.rtc2jira.storage;

import java.io.Closeable;
import java.io.IOException;
import java.util.function.Consumer;

import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.server.OServer;
import com.orientechnologies.orient.server.OServerMain;

public class StorageEngine implements Closeable {

  private OServer server;
  private AttachmentStorage attachmentStorage;

  public StorageEngine() throws Exception {
    server = OServerMain.create();
    server.startup(Thread.currentThread().getContextClassLoader()
        .getResourceAsStream("orientconf.xml"));
    server.activate();
    attachmentStorage = new AttachmentStorage();
  }

  public void withDB(Consumer<ODatabaseDocumentTx> doWithDB) {
    try (ODatabaseDocumentTx db = new ODatabaseDocumentTx("plocal:./databases/rtc2jira")) {
      if (!db.exists()) {
        db.create();
        OClass workItemClass = db.getMetadata().getSchema().createClass("WORKITEM");
        workItemClass.setStrictMode(false);
        workItemClass.createProperty("ID", OType.STRING).setMandatory(true).setNotNull(true);
        workItemClass.createIndex("workitem_ID_IDX", OClass.INDEX_TYPE.UNIQUE, "ID");
      } else {
        db.open("admin", "admin");
      }
      doWithDB.accept(db);
    }
  }

  public AttachmentStorage getAttachmentStorage() {
    return attachmentStorage;
  }

  @Override
  public void close() throws IOException {
    server.shutdown();
  }
}