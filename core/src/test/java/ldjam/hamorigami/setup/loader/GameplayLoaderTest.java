package ldjam.hamorigami.setup.loader;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglFiles;
import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.anchor.AnchorManager;
import ldjam.hamorigami.context.HamorigamiContext;
import ldjam.hamorigami.cutscene.Cutscene;
import ldjam.hamorigami.setup.GameplaySetup;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GameplayLoaderTest {

   private GameplaySetupLoader loader;
   private GameplaySetup setup;

   @Mock
   private HamorigamiContext context;

   @Before
   public void beforeEach() throws IOException {
      Gdx.files = new LwjglFiles();
      Application application = mock(Application.class);
      doAnswer(new Answer<Void>() {
         @Override
         public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
            String tag = (String) invocationOnMock.getArguments()[0];
            String message = (String) invocationOnMock.getArguments()[1];
            throw new RuntimeException("Unexpected error log found: tag=" + tag + ", message=" + message);
         }
      }).when(application).error(anyString(), anyString());
      Gdx.app = application;
      AnchorManager anchorManager = new AnchorManager();
      anchorManager.addAnchor("tree", new GameObject());
      when(context.getAnchorManager()).thenReturn(anchorManager);
      this.loader = new GameplaySetupLoader(context);
      this.setup = loader.load("mock-game.play");
   }

   @Test
   public void testLoadGameplayFile() {
      assertThat(setup.getCurrentDaySetup()).isNotNull();
   }

   @Test
   public void testNumberOfDays() {
      assertThat(setup.getNumberOfDays()).isEqualTo(3);
   }

   @Test
   public void testFirstDayHasMorningCutscene() {
      assertThat(setup.getCurrentDaySetup().getStartCutscene()).isNotNull();
      assertThat(setup.getCurrentDaySetup().getEndCutscene()).isNull();
      final Cutscene cutscene = setup.getCurrentDaySetup().getStartCutscene();
      assertThat(cutscene.size()).isEqualTo(7);
      assertThat(setup.getCurrentDaySetup().getSpawns()).hasSize(28);
   }

   @Test
   public void testSecondDayHasEveningCutscene() {
      setup.triggerNextDay();
      assertThat(setup.getCurrentDaySetup().getStartCutscene()).isNull();
      assertThat(setup.getCurrentDaySetup().getEndCutscene()).isNotNull();
      assertThat(setup.getCurrentDaySetup().getSpawns()).hasSize(9);
      final Cutscene cutscene = setup.getCurrentDaySetup().getEndCutscene();
      assertThat(cutscene.size()).isEqualTo(1);
   }

   @Test
   public void testThirdDayHasNoCutscene() {
      setup.triggerNextDay();
      setup.triggerNextDay();
      assertThat(setup.getCurrentDaySetup().getStartCutscene()).isNull();
      assertThat(setup.getCurrentDaySetup().getEndCutscene()).isNull();
   }

   @Test
   public void testThirdDayHasSpawns() {
      setup.triggerNextDay();
      setup.triggerNextDay();
      assertThat(setup.getCurrentDaySetup().getSpawns()).hasSize(18);
   }
}
