package uk.ac.bbsrc.tgac.miso.webapp.integrationtest;

import static org.junit.Assert.assertNotNull;
import static uk.ac.bbsrc.tgac.miso.webapp.integrationtest.util.FormPageTestUtils.*;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Maps;

import uk.ac.bbsrc.tgac.miso.core.data.IlluminaRun;
import uk.ac.bbsrc.tgac.miso.core.data.PacBioRun;
import uk.ac.bbsrc.tgac.miso.core.data.Run;
import uk.ac.bbsrc.tgac.miso.core.data.SequencingParameters;
import uk.ac.bbsrc.tgac.miso.core.util.LimsUtils;
import uk.ac.bbsrc.tgac.miso.webapp.integrationtest.page.RunPage;
import uk.ac.bbsrc.tgac.miso.webapp.integrationtest.page.RunPage.Field;;

public class RunPageIT extends AbstractIT {

  @Before
  public void setup() {
    loginAdmin();
  }

  @Test
  public void testCreatePacBio() throws Exception {
    RunPage page1 = RunPage.getForCreate(getDriver(), getBaseUrl(), 5001L);

    // default values
    Map<RunPage.Field, String> fields = Maps.newLinkedHashMap();
    fields.put(Field.ID, "Unsaved");
    fields.put(Field.NAME, "Unsaved");
    fields.put(Field.ALIAS, null);
    fields.put(Field.PLATFORM, "PacBio");
    fields.put(Field.SEQUENCER, "PacBio_SR - PacBio RS II");
    fields.put(Field.SEQ_PARAMS, "SELECT");
    fields.put(Field.DESCRIPTION, null);
    fields.put(Field.FILE_PATH, null);
    fields.put(Field.STATUS, "Unknown");
    fields.put(Field.START_DATE, null);
    fields.put(Field.COMPLETION_DATE, null);
    assertFieldValues("default values", fields, page1);

    // enter run info
    Map<RunPage.Field, String> changes = Maps.newLinkedHashMap();
    changes.put(Field.ALIAS, "Test_PacBio_Run_Creation");
    changes.put(Field.SEQ_PARAMS, "Custom (see notes)");
    changes.put(Field.DESCRIPTION, "test run creation");
    changes.put(Field.FILE_PATH, "/nowhere");
    changes.put(Field.STATUS, "Running");
    changes.put(Field.START_DATE, "2017-09-01");
    page1.setFields(changes);

    fields.putAll(changes);
    assertFieldValues("pre-save", fields, page1);

    RunPage page2 = page1.save();
    fields.remove(Field.ID);
    fields.remove(Field.NAME);
    assertFieldValues("post-save", fields, page2);
    long savedId = Long.parseLong(page2.getField(Field.ID));
    Run savedRun = (Run) getSession().get(PacBioRun.class, savedId);
    fields.put(Field.ID, Long.toString(savedId));
    fields.put(Field.NAME, "RUN" + savedId);
    assertRunAttributes(fields, savedRun);
  }

  @Test
  public void testCreateIllumina() throws Exception {
    RunPage page1 = RunPage.getForCreate(getDriver(), getBaseUrl(), 5002L);

    // default values
    Map<RunPage.Field, String> fields = Maps.newLinkedHashMap();
    fields.put(Field.ID, "Unsaved");
    fields.put(Field.NAME, "Unsaved");
    fields.put(Field.ALIAS, null);
    fields.put(Field.PLATFORM, "Illumina");
    fields.put(Field.SEQUENCER, "HiSeq_SR - Illumina HiSeq 2500");
    fields.put(Field.SEQ_PARAMS, "SELECT");
    fields.put(Field.DESCRIPTION, null);
    fields.put(Field.FILE_PATH, null);
    fields.put(Field.STATUS, "Unknown");
    fields.put(Field.START_DATE, null);
    fields.put(Field.COMPLETION_DATE, null);
    assertFieldValues("default values", fields, page1);

    // enter run info
    Map<RunPage.Field, String> changes = Maps.newLinkedHashMap();
    changes.put(Field.ALIAS, "Test_Illumina_Run_Creation");
    changes.put(Field.SEQ_PARAMS, "Rapid Run 2x151");
    changes.put(Field.DESCRIPTION, "test Illumina run creation");
    changes.put(Field.FILE_PATH, "/nowhere");
    changes.put(Field.STATUS, "Started");
    changes.put(Field.START_DATE, "2017-09-01");
    page1.setFields(changes);

    fields.putAll(changes);
    assertFieldValues("pre-save", fields, page1);

    RunPage page2 = page1.save();
    fields.remove(Field.ID);
    fields.remove(Field.NAME);
    assertFieldValues("post-save", fields, page2);
    long savedId = Long.parseLong(page2.getField(Field.ID));
    Run savedRun = (Run) getSession().get(IlluminaRun.class, savedId);
    fields.put(Field.ID, Long.toString(savedId));
    fields.put(Field.NAME, "RUN" + savedId);
    assertRunAttributes(fields, savedRun);
  }

  @Test
  public void testChangeValues() throws Exception {
    // goal: change all changeable values
    RunPage page = RunPage.getForEdit(getDriver(), getBaseUrl(), 5001);

    // check initial values
    Map<Field, String> fields = Maps.newLinkedHashMap();
    fields.put(Field.ID, "5001");
    fields.put(Field.NAME, "RUN5001");
    fields.put(Field.ALIAS, "Change_Values_Run");
    fields.put(Field.PLATFORM, "Illumina");
    fields.put(Field.SEQUENCER, "HiSeq_SR - Illumina HiSeq 2500");
    fields.put(Field.SEQ_PARAMS, "Rapid Run 2x151");
    fields.put(Field.DESCRIPTION, "description");
    fields.put(Field.FILE_PATH, "/filePath");
    fields.put(Field.NUM_CYCLES, "75");
    fields.put(Field.CALL_CYCLE, "35");
    fields.put(Field.IMG_CYCLE, "34");
    fields.put(Field.SCORE_CYCLE, "33");
    fields.put(Field.PAIRED_END, "true");
    fields.put(Field.STATUS, "Running");
    fields.put(Field.START_DATE, "2017-09-05");
    fields.put(Field.COMPLETION_DATE, null);
    assertFieldValues("loaded", fields, page);

    // make changes
    Map<Field, String> changes = Maps.newLinkedHashMap();
    changes.put(Field.ALIAS, "Changed_Alias_Run");
    changes.put(Field.SEQ_PARAMS, "1x151");
    changes.put(Field.DESCRIPTION, "changed description");
    changes.put(Field.FILE_PATH, "/new/filePath");
    changes.put(Field.NUM_CYCLES, "100");
    changes.put(Field.CALL_CYCLE, "99");
    changes.put(Field.IMG_CYCLE, "80");
    changes.put(Field.SCORE_CYCLE, "183");
    changes.put(Field.PAIRED_END, "false");
    changes.put(Field.STATUS, "Failed");
    changes.put(Field.COMPLETION_DATE, "2017-09-10");
    page.setFields(changes);

    // copy unchanged
    fields.forEach((key, val) -> {
      if (!changes.containsKey(key)) changes.put(key, val);
    });
    assertFieldValues("changes pre-save", changes, page);

    RunPage page2 = page.save();
    assertNotNull(page2);
    assertFieldValues("changes post-save", changes, page2);

    Run run = (Run) getSession().get(Run.class, 5001L);
    assertRunAttributes(changes, run);
  }

  @Test
  public void testAddExistingContainer() throws Exception {
    // goal: add an existing container to a run
    RunPage page = RunPage.getForEdit(getDriver(), getBaseUrl(), 5002L);
    RunPage page2 = page.addContainer("EXISTING", "Illumina");
    assertNotNull(page2);
  }

  private void assertRunAttributes(Map<RunPage.Field, String> expectedValues, Run run) {
    assertAttribute(Field.ID, expectedValues, Long.toString(run.getId()));
    assertAttribute(Field.NAME, expectedValues, run.getName());
    assertAttribute(Field.ALIAS, expectedValues, run.getAlias());
    assertAttribute(Field.PLATFORM, expectedValues, run.getPlatformType().getKey());
    assertAttribute(Field.SEQUENCER, expectedValues,
        run.getSequencerReference().getName() + " - " + run.getSequencerReference().getPlatform().getInstrumentModel());
    assertAttribute(Field.SEQ_PARAMS, expectedValues, nullOrGet(run.getSequencingParameters(), SequencingParameters::getName));
    assertAttribute(Field.DESCRIPTION, expectedValues, nullOrToString(run.getDescription()));
    assertAttribute(Field.FILE_PATH, expectedValues, run.getFilePath());
    assertAttribute(Field.STATUS, expectedValues, run.getHealth().getKey());
    assertAttribute(Field.START_DATE, expectedValues, LimsUtils.formatDate(run.getStartDate()));
    assertAttribute(Field.COMPLETION_DATE, expectedValues, LimsUtils.formatDate(run.getCompletionDate()));
    if (run instanceof IlluminaRun) assertIlluminaRunAttributes(expectedValues, (IlluminaRun) run);
  }

  private void assertIlluminaRunAttributes(Map<RunPage.Field, String> expectedValues, IlluminaRun run) {
    assertAttribute(Field.NUM_CYCLES, expectedValues, nullOrToString(run.getNumCycles()));
    assertAttribute(Field.CALL_CYCLE, expectedValues, nullOrToString(run.getCallCycle()));
    assertAttribute(Field.IMG_CYCLE, expectedValues, nullOrToString(run.getImgCycle()));
    assertAttribute(Field.SCORE_CYCLE, expectedValues, nullOrToString(run.getScoreCycle()));
    assertAttribute(Field.PAIRED_END, expectedValues, Boolean.toString(run.getPairedEnd()));
  }
}
